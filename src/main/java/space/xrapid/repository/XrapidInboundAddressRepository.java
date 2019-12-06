package space.xrapid.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import space.xrapid.domain.Currency;
import space.xrapid.domain.XrapidInboundAddress;

import java.util.Set;

@Repository
public interface XrapidInboundAddressRepository extends JpaRepository<XrapidInboundAddress, Integer> {
    boolean existsByAddressAndTag(String address, long tag);
    XrapidInboundAddress getByAddressAndTag(String address, long tag);
    boolean existsByAddressAndTagAndSourceFiatAndRecurrenceGreaterThan(String address, Long tag, Currency sourceFiat, int minRecurrence);
}
