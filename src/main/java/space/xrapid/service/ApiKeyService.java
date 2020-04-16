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

    @Transactional
    public ApiKey generateApiKey(long ttlInDayes) {
        String apiKey = UUID.randomUUID().toString();

        Date expiration = new Date(new Date().getTime() + ttlInDayes * 24 * 60 * 60 * 1000);

        return apiKeyRepository.save(ApiKey.builder().key(apiKey).expiration(expiration).build());
    }
}
