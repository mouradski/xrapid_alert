package space.xrapid.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import space.xrapid.domain.XrapidInboundAddress;

public interface XrapidInboundAddressRepository extends JpaRepository<XrapidInboundAddress, Integer> {
    boolean existsByAddressAndTag(String address, Long tag);
}
