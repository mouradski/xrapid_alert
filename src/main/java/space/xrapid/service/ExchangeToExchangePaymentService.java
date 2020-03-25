package space.xrapid.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import space.xrapid.domain.Currency;
import space.xrapid.domain.ExchangeToExchangePayment;
import space.xrapid.domain.Stats;
import space.xrapid.repository.ExchangeToExchangePaymentRepository;

import javax.persistence.criteria.Predicate;
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
    public boolean save(ExchangeToExchangePayment exchangeToExchangePayment) {

        boolean exist = repository.existsByTransactionHash(exchangeToExchangePayment.getTransactionHash());

        if (exist) {
            return false;
        }

        exchangeToExchangePayment.setTradeOutIds(exchangeToExchangePayment.getTradeOutIds());
        exchangeToExchangePayment.setTradeIds(exchangeToExchangePayment.getTradeIds());
        repository.save(exchangeToExchangePayment);

        return true;
    }

    @Cacheable(value = "statsCache", key = "1")
    public Stats calculateStats() {
        try {
            OffsetDateTime today = OffsetDateTime.now(ZoneOffset.UTC).withMinute(0).withHour(0).withSecond(0).withNano(0);
            OffsetDateTime now = OffsetDateTime.now(ZoneOffset.UTC);
            Double allTimeVolume = repository.getAllTimeVolume();
            Double todayVolume = repository.getVolumeBetween(today.toEpochSecond() * 1000, now.toEpochSecond() * 1000);

            String[] days = new String[21];

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

            double[] volumePerDay = new double[21];
            volumePerDay[20] = roundVolume(todayVolume);

            days[20] = "Today";

            for (int i = 19; i >= 0; i--) {
                Double volume = repository.getVolumeBetween(today.minusDays(1 * (i + 1)).toEpochSecond() * 1000, today.minusDays(1 * (i + 1)).plusDays(1).toEpochSecond() * 1000);

                if (volume == null) {
                    volumePerDay[19 - i] = 0;

                } else {
                    volumePerDay[19 - i] = roundVolume(volume);
                }

                days[19 - i] = today.minusDays(1 * (i + 1)).toString().substring(2, 10);
            }

            calculateDailyVolumes(false);
            
            double athDayVolume = dailyVolumes.values().stream()
                    .mapToDouble(v -> v.doubleValue())
                    .max().getAsDouble();

            return Stats.builder()
                    .allTimeVolume(roundVolume(allTimeVolume))
                    .todayVolume(roundVolume(todayVolume))
                    .topVolumes(volumes)
                    .allTimeFrom(repository.getFirstOdl().getDateTime())
                    .last5DaysOdlVolume(volumePerDay)
                    .athDaylyVolume(athDayVolume)
                    .days(days)
                    .build();
        } catch (Exception e) {
            return null;
        }
    }

    @Scheduled(fixedDelay = 3600000)
    public void forceUpdateAths() {
        calculateDailyVolumes(true);
    }

    private void calculateDailyVolumes(boolean force) {
        OffsetDateTime today = OffsetDateTime.now(ZoneOffset.UTC).withMinute(0).withHour(0).withSecond(0).withNano(0);
        OffsetDateTime day = today.minusDays(1);

        if (dailyVolumes.isEmpty() || force) {
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

    public List<ExchangeToExchangePayment> search(Long from, Long to, Currency source, Currency destination) {
        return repository.findAll((Specification<ExchangeToExchangePayment>) (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (source != null) {
                predicates.add(criteriaBuilder.and(criteriaBuilder.equal(root.get("sourceFiat"), source)));
            }

            if (destination != null) {
                predicates.add(criteriaBuilder.and(criteriaBuilder.equal(root.get("destinationFiat"), destination)));
            }

            if (from != null) {
                predicates.add(criteriaBuilder.and(criteriaBuilder.ge(root.get("timestamp"), from)));
            }

            if (to != null) {
                predicates.add(criteriaBuilder.and(criteriaBuilder.le(root.get("timestamp"), to)));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
        });

    }

    private double roundVolume(double volume) {
        return Math.round(volume * 100.0) / 100.0;
    }

    @Cacheable(value = "lastOdlCache", key = "1")
    public List<ExchangeToExchangePayment> getLasts() {
        List<ExchangeToExchangePayment> payments =  repository.findTop(350);

        payments.forEach(payment -> {
            if (payment.getTradeIds() != null && !payment.getTradeIds().isEmpty()) {
                payment.setXrpToFiatTradeIds(Arrays.asList(payment.getTradeIds().split(";")));
            }

            if (payment.getTradeOutIds() != null && !payment.getTradeOutIds().isEmpty()) {
                payment.setFiatToXrpTradeIds(Arrays.asList(payment.getTradeOutIds().split(";")));
            }

        });

        return payments;
    }


    public List<ExchangeToExchangePayment> getPayments(long from, long to) {
        return repository.findByDate(from, to);
    }
}
