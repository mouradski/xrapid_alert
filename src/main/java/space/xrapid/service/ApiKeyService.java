package space.xrapid.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import space.xrapid.domain.ApiKey;
import space.xrapid.exception.UnauthorizedException;
import space.xrapid.repository.ApiKeyRepository;

import java.util.Date;
import java.util.UUID;

@Service
public class ApiKeyService {

    @Autowired
    private ApiKeyRepository apiKeyRepository;

    @Transactional(readOnly = true)
    @Cacheable(value = "apiKey", key = "#key")
    public void validateKey(final String key) {
        if (key == null || !apiKeyRepository.existsByKeyAndExpirationIsAfter(key, new Date())) {
            throw new UnauthorizedException();
        }
    }

    @Transactional(readOnly = true)
    public ApiKey getApiKey(final String key) {
      return apiKeyRepository.getOne(key);
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "apiKeyMaster", key = "#key")
    public void validateMasterKey(final String key) {
        if (key == null || !apiKeyRepository.existsByKeyAndMaster(key, true)) {
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
}
