package space.xrapid.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import space.xrapid.domain.Exchange;
import space.xrapid.domain.ExchangeToExchangePayment;
import space.xrapid.domain.Stats;
import space.xrapid.repository.ExchangeToExchangePaymentRepository;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class ExchangeToExchangePaymentService {

    @Autowired
    private ExchangeToExchangePaymentRepository repository;

    @Transactional
    public boolean save(ExchangeToExchangePayment exchangeToExchangePayment) {

        if (repository.existsByTransactionHash(exchangeToExchangePayment.getTransactionHash())) {
            return false;
        }

        repository.save(exchangeToExchangePayment);

        return true;
    }

    public Stats calculateStats() {
        OffsetDateTime today = OffsetDateTime.now(ZoneOffset.UTC).withMinute(0).withHour(0).withSecond(0).withNano(0);
        OffsetDateTime now = OffsetDateTime.now(ZoneOffset.UTC);
        double allTimeVolume = repository.getAllTimeVolume();
        Double todayVolume = repository.getVolumeBetween(today.toEpochSecond() * 1000, now.toEpochSecond() * 1000);

        if (todayVolume == null) {
            todayVolume = 0d;
        }

        Map<String, Double> volumes = new HashMap<>();

        List<Exchange> exchanges = Arrays.stream(Exchange.values()).filter(Exchange::isConfirmed).collect(Collectors.toList());
        for (Exchange source : exchanges) {
            for (Exchange destination : exchanges) {
                if (source.equals(destination)) {
                    continue;
                }

                try {
                    Double volume = repository.getVolumeBySourceAndDestinationBetween(source.toString(), destination.toString(),
                            now.plusDays(-1).toEpochSecond() * 1000, now.toEpochSecond() * 1000);
                    if (volume != null) {
                        String key = source.getLocalFiat() + "-" + destination.getLocalFiat();
                        if (volumes.containsKey(key)) {
                            volumes.put(key,  roundVolume(volume) + volumes.get(key));
                        } else {
                            volumes.put(key,  roundVolume(volume));
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        double[] volumePerDay = new double[6];
        volumePerDay[5] = roundVolume(todayVolume);
        for (int i = 4; i >= 0; i--) {
            Double volume = repository.getVolumeBetween(today.plusDays(-1 * (i + 1)).toEpochSecond() * 1000, today.plusDays(-1 * (i + 1)).plusDays(1).toEpochSecond() * 1000);

            if (volume == null) {
                volumePerDay[4 - i] = 0;

            } else {
                volumePerDay[4 - i] = roundVolume(volume);
            }
        }

        return Stats.builder()
                .allTimeVolume(roundVolume(allTimeVolume))
                .todayVolume(roundVolume(todayVolume))
                .topVolumes(volumes)
                .allTimeFrom(repository.getFirstOdl().getDateTime())
                .last5DaysOdlVolume(volumePerDay)
                .build();
    }

    private double roundVolume(double volume) {
        return Math.round(volume * 100.0) / 100.0;
    }

    public List<ExchangeToExchangePayment> getLasts() {
        return repository.findTop(150);
    }
}
