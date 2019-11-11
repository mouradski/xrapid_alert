package space.xrapid.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import space.xrapid.domain.Currency;
import space.xrapid.domain.Exchange;
import space.xrapid.domain.ExchangeToExchangePayment;
import space.xrapid.domain.Stats;
import space.xrapid.repository.ExchangeToExchangePaymentRepository;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional(readOnly = true)
public class ExchangeToExchangePaymentService {

    @Autowired
    private ExchangeToExchangePaymentRepository repository;

    private Map<OffsetDateTime, Double> dailyVolumes = new HashMap<>();

    @Transactional
    public boolean save(ExchangeToExchangePayment exchangeToExchangePayment, boolean force) {

        boolean exist = repository.existsByTransactionHash(exchangeToExchangePayment.getTransactionHash());
        if (exist && !force) {
            return false;
        }

        if (exist && force) {
            ExchangeToExchangePayment exchangeToExchangePaymentDb = repository.getByTransactionHash(exchangeToExchangePayment.getTransactionHash());
            exchangeToExchangePaymentDb.setTradeOutIds(exchangeToExchangePayment.getTradeOutIds());
            exchangeToExchangePaymentDb.setTradeIds(exchangeToExchangePayment.getTradeIds());
            repository.save(exchangeToExchangePaymentDb);
            return true;
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

        List<Currency> currencies = Arrays.stream(Currency.values()).collect(Collectors.toList());
        for (Currency source : currencies) {
            for (Currency destination : currencies) {
                if (source.equals(destination)) {
                    continue;
                }

                try {
                    Double volume = repository.getVolumeBySourceFiatAndDestinationFiatBetween(source.toString(), destination.toString(),
                            now.minusDays(1).toEpochSecond() * 1000, now.toEpochSecond() * 1000);
                    if (volume != null) {
                        String key = source + "-" + destination;
                        if (volumes.containsKey(key)) {
                            volumes.put(key, roundVolume(volume) + volumes.get(key));
                        } else {
                            volumes.put(key, roundVolume(volume));
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
            Double volume = repository.getVolumeBetween(today.minusDays(1 * (i + 1)).toEpochSecond() * 1000, today.minusDays(1 * (i + 1)).plusDays(1).toEpochSecond() * 1000);

            if (volume == null) {
                volumePerDay[4 - i] = 0;

            } else {
                volumePerDay[4 - i] = roundVolume(volume);
            }
        }

        calculateDailyVolumes();

        double athDayVolume = dailyVolumes.values().stream()
                .mapToDouble(v -> v.doubleValue())
                .peek(System.out::println)
                .max().getAsDouble();

        return Stats.builder()
                .allTimeVolume(roundVolume(allTimeVolume))
                .todayVolume(roundVolume(todayVolume))
                .topVolumes(volumes)
                .allTimeFrom(repository.getFirstOdl().getDateTime())
                .last5DaysOdlVolume(volumePerDay)
                .athDaylyVolume(athDayVolume)
                .build();
    }

    private void calculateDailyVolumes() {
        OffsetDateTime today = OffsetDateTime.now(ZoneOffset.UTC).withMinute(0).withHour(0).withSecond(0).withNano(0);
        OffsetDateTime day = today.minusDays(1);
        if (dailyVolumes.isEmpty()) {
            for (int i = 0; i < 365; i++) {
                Double volume = repository.getVolumeBetween(day.toEpochSecond() * 1000, day.plusDays(1).toEpochSecond() * 1000);
                dailyVolumes.put(day, volume == null ? 0 : volume);
                day = day.minusDays(1);
            }
        } else {
            OffsetDateTime latestCalculatedDay = dailyVolumes.keySet().stream().max(Comparator.comparing(OffsetDateTime::toEpochSecond)).get();
            day = latestCalculatedDay.plusDays(1);

            while (day.isBefore(today)) {
                Double volume = repository.getVolumeBetween(day.toEpochSecond() * 1000, day.plusDays(1).toEpochSecond() * 1000);

                dailyVolumes.put(day, volume == null ? 0 : volume);

                day = day.plusDays(1);
            }
        }
    }

    private double roundVolume(double volume) {
        return Math.round(volume * 100.0) / 100.0;
    }

    public List<ExchangeToExchangePayment> getLasts() {
        return repository.findTop(300);
    }
}
