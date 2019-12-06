package space.xrapid.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import space.xrapid.domain.XrapidInboundAddress;

import java.util.Set;

@Repository
public interface XrapidInboundAddressRepository extends JpaRepository<XrapidInboundAddress, Integer> {
    boolean existsByAddressAndTag(String address, Long tag);
    XrapidInboundAddress getByAddressAndTag(String address, Long tag);
    boolean existsByAddressAndTagAndRecurrenceGreaterThan(String address, Long tag, int minRecurrence);
}
