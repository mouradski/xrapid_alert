package space.xrapid.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import space.xrapid.domain.DestinationTagRepeat;
import space.xrapid.repository.DestinationTagRepeatRepository;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DestinationTagRepeatService {

    @Autowired
    private DestinationTagRepeatRepository destinationTagRepeatRepository;

    @Transactional
    public void add(String sourceAddress, String destinationAddress, String source,
                    String destination, Long repeat, Long destinationTag, Double sum) {
        DestinationTagRepeat destinationTagRepeat = destinationTagRepeatRepository
                .getBySourceAddressAndDestinationAddressAndDestinationTag(sourceAddress, destinationAddress,
                        destinationTag);

        if (destinationTagRepeat == null) {
            destinationTagRepeat = DestinationTagRepeat.builder()
                    .sourceAddress(sourceAddress)
                    .destinationAddress(destinationAddress)
                    .source(source)
                    .destination(destination)
                    .destinationTag(destinationTag)
                    .repeated(repeat)
                    .sum(sum)
                    .build();
        } else {
            destinationTagRepeat.setRepeated(destinationTagRepeat.getRepeated() + repeat);
            destinationTagRepeat.setSum(destinationTagRepeat.getSum() + sum);
        }

        destinationTagRepeatRepository.save(destinationTagRepeat);
    }

    @Transactional
    public void purge() {
        destinationTagRepeatRepository.deleteAll();
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "tagsrepeat", key = "1")
    public List<DestinationTagRepeat> getAll() {
        return destinationTagRepeatRepository.findAll().stream()
                .sorted(Comparator.comparing(DestinationTagRepeat::getRepeated).reversed())
                .collect(Collectors.toList());
    }
}
