package space.xrapid.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import space.xrapid.domain.ApiKey;
import space.xrapid.exception.MomentarilyBlockedException;
import space.xrapid.exception.UnauthorizedException;
import space.xrapid.repository.ApiKeyRepository;

import java.util.*;

@Service
public class ApiKeyService {

    @Autowired
    private ApiKeyRepository apiKeyRepository;

    private Map<String, Set<String>> ips = new HashMap<>();
    private Map<String, Integer> calls = new HashMap<>();
    private Set<String> blockedKeys = new HashSet();


    @Cacheable(value = "apiKey", key = "#key")
    public boolean validateKey(final String key) {
        if (key == null) {
            throw new UnauthorizedException();
        }

        ApiKey apiKey = apiKeyRepository.getByKey(key).orElse(null);

        if (apiKey == null || apiKey.isBan() || apiKey.getExpiration().getTime() < new Date().getTime()) {
            throw new UnauthorizedException();
        }

        return apiKey.isMaster();
    }

    @Transactional(readOnly = true)
    public void validateKey(final String key, String ip) {
        if (!validateKey(key)) {
            checkCallLimits(key, ip);
        }
    }

    private void checkCallLimits(String key, String ip) {
        if (!ips.containsKey(key)) {
            ips.put(key, new HashSet<>());
        }

        if (!calls.containsKey(key)) {
            calls.put(key, 0);
        }

        ips.get(key).add(ip);
        calls.put(key, calls.get(key) + 1);

        if (ips.get(key).size() >= 3) {
            ApiKey apiKey = apiKeyRepository.getOne(key);
            apiKey.setBan(true);
            apiKeyRepository.save(apiKey);
            throw new UnauthorizedException();
        } else if (ips.get(key).size() >= 2) {
            blockedKeys.add(key);
            throw new MomentarilyBlockedException();
        }

        if (blockedKeys.contains(key)) {
            throw new MomentarilyBlockedException();
        }

        if (calls.get(key) > 60) {
            throw new MomentarilyBlockedException();
        }
    }

    @Transactional(readOnly = true)
    public ApiKey getApiKey(final String key) {
        return apiKeyRepository.getOne(key);
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "apiKeyMaster", key = "#key")
    public void validateMasterKey(final String key) {
        if (key == null || !apiKeyRepository.existsByKeyAndMasterAndBanIsFalse(key, true)) {
            throw new UnauthorizedException();
        }
    }

    @Transactional
    public ApiKey renewKey(String key, Date expiration) {
        ApiKey apiKey = apiKeyRepository.getOne(key);

        apiKey.setExpiration(expiration);

        return apiKeyRepository.save(apiKey);
    }

    @Transactional
    public ApiKey generateApiKey(Date expiration) {
        String apiKey = UUID.randomUUID().toString();

        return apiKeyRepository.save(ApiKey.builder().key(apiKey).expiration(expiration).master(false).build());
    }

    @Scheduled(fixedDelay = 60000)
    public void resetLimitsCounter() {
        this.ips.clear();
        this.calls.clear();
    }

    @Scheduled(fixedDelay = 120000)
    public void unblockKeys() {
        blockedKeys.clear();
    }
}
