package space.xrapid.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import space.xrapid.domain.DestinationTagRepeat;

@Repository
public interface DestinationTagRepeatRepository extends JpaRepository<DestinationTagRepeat, Integer> {
    DestinationTagRepeat getBySourceAddressAndDestinationAddressAndDestinationTag(String source, String destination, Long destinationTag);
}
