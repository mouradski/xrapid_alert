package space.xrapid.repository;

import java.util.Date;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import space.xrapid.domain.ApiKey;

@Repository
public interface ApiKeyRepository extends JpaRepository<ApiKey, String> {

  boolean existsByKey(String key);

  boolean existsByKeyAndExpirationIsAfter(String key, Date now);

  boolean existsByKeyAndMasterAndBanIsFalse(String key, boolean master);

  Optional<ApiKey> getByKey(String key);
}
