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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        double todayVolume = repository.getVolumeBetween(today.toEpochSecond() * 1000, now.toEpochSecond() * 1000);

        Map<String, Double> volumes = new HashMap<>();

        for (Exchange source : Exchange.values()) {
            for (Exchange destination : Exchange.values()) {
                if (source.equals(destination)) {
                    continue;
                }

                try {
                    Double volume = repository.getVolumeBySourceAndDestinationBetween(source.toString(), destination.toString(), today.toEpochSecond() * 1000, now.toEpochSecond() * 1000);

                    if (volume != null) {
                        volumes.put(source + "_" + destination, volume);
                    }
                }catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }

        int i = 0;

        double[] volumePerDay = new double[5];

        while (i < 5) {
            Double volume = repository.getVolumeBetween(today.plusDays(-1 * (i+1)).toEpochSecond() * 1000, today.plusDays(-1 * (i+1)).plusDays(1).toEpochSecond() * 1000);

            if (volume == null) {
                volumePerDay[i] = 0;

            } else {
                volumePerDay[i] = volume;
            }

            i++;
        }

        return Stats.builder()
                .allTimeVolume(allTimeVolume)
                .todayVolume(todayVolume)
                .topVolumes(volumes)
                .last5DaysOdlVolume(volumePerDay)
                .build();
    }

    public List<ExchangeToExchangePayment> getLasts() {
        return repository.findTop(20);
    }
}
