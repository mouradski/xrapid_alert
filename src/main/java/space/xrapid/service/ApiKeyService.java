package space.xrapid.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import space.xrapid.exception.UnauthorizedException;
import space.xrapid.repository.ApiKeyRepository;

@Service
public class ApiKeyService {

    @Autowired
    private ApiKeyRepository apiKeyRepository;

    public void validateKey(String key) {
        if (key == null || !apiKeyRepository.existsByKey(key)) {
            throw new UnauthorizedException();
        }
    }
}
