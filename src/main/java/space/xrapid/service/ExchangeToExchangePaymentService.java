package space.xrapid.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import space.xrapid.domain.Currency;
import space.xrapid.domain.*;
import space.xrapid.repository.ExchangeToExchangePaymentRepository;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
@Slf4j
@Transactional(readOnly = true)
public class ExchangeToExchangePaymentService {

    @Autowired
    private ExchangeToExchangePaymentRepository repository;

    @Autowired
    private EntityManager entityManager;

    private GlobalStats globalStats;

    private Map<OffsetDateTime, Double> dailyVolumes = new HashMap<>();

    @Transactional
    public boolean save(ExchangeToExchangePayment exchangeToExchangePayment) {

        if (repository.existsByTransactionHash(exchangeToExchangePayment.getTransactionHash())) {
            return false;
        }

        exchangeToExchangePayment.setTradeOutIds(exchangeToExchangePayment.getTradeOutIds());
        exchangeToExchangePayment.setTradeIds(exchangeToExchangePayment.getTradeIds());
        repository.save(exchangeToExchangePayment);

        return true;
    }

    @Cacheable(value = "statsCache", key = "#daysNbr")
    public Stats calculateStats(int daysNbr) {
        try {
            OffsetDateTime today = OffsetDateTime.now(ZoneOffset.UTC).withMinute(0).withHour(0)
                    .withSecond(0).withNano(0);
            OffsetDateTime now = OffsetDateTime.now(ZoneOffset.UTC);
            Double allTimeVolume = repository.getAllTimeVolume();
            Double todayVolume = repository
                    .getVolumeBetween(today.toEpochSecond() * 1000, now.toEpochSecond() * 1000);

            String[] days = new String[daysNbr];

            if (todayVolume == null) {
                todayVolume = 0d;
            }

            Map<String, Double> volumes = new HashMap<>();

            Set<String> currencies = allUsedCurrencies();
            for (String source : currencies) {
                for (String destination : currencies) {

                    long startTimestamp = now.minusDays(1).toEpochSecond() * 1000;
                    long endTimestamp = now.toEpochSecond() * 1000;

                    if (source.equals(destination)) {
                        continue;
                    }

                    try {
                        Double volume = repository
                                .getVolumeBySourceFiatAndDestinationFiatBetween(source, destination,
                                        startTimestamp, endTimestamp);
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

            double[] volumePerDay = new double[daysNbr];
            volumePerDay[daysNbr - 1] = roundVolume(todayVolume);

            days[daysNbr - 1] = "Today";

            for (int i = daysNbr - 2; i >= 0; i--) {
                Double volume = repository.getVolumeBetween(today.minusDays(i + 1).toEpochSecond() * 1000,
                        today.minusDays(i + 1).plusDays(1).toEpochSecond() * 1000);

                if (volume == null) {
                    volumePerDay[daysNbr - 2 - i] = 0;

                } else {
                    volumePerDay[daysNbr - 2 - i] = roundVolume(volume);
                }

                days[daysNbr - 2 - i] = today.minusDays(i + 1).toString().substring(2, 10);
            }

            calculateDailyVolumes(false);

            double athDayVolume = dailyVolumes.values().stream()
                    .mapToDouble(v -> v)
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

    private Set<String> allUsedCurrencies() {
        Set<String> currencies = new HashSet<>();
        repository.getAllDestinationCurrencies().forEach(currency -> {
            if (currency != null) {
                currencies.add(currency);
            }
        });
        repository.getAllSourceCurrencies().forEach(currency -> {
            if (currency != null) {
                currencies.add(currency);
            }
        });
        return currencies;
    }


    public GlobalStats calculateGlobalStats(boolean forceUpdate) {

        if (!forceUpdate && globalStats != null) {
            return globalStats;
        }

        OffsetDateTime today = OffsetDateTime.now(ZoneOffset.UTC).withMinute(0).withHour(0)
                .withSecond(0).withNano(0);
        ExchangeToExchangePayment oldestPayment = repository.findTopByOrderByTimestampAsc();

        int daysNbr = (int) oldestPayment.getDateTime().until(today, ChronoUnit.DAYS) + 2;

        try {

            OffsetDateTime now = OffsetDateTime.now(ZoneOffset.UTC);

            Double allTimeVolume = repository.getAllTimeVolume();
            Double todayVolume = repository
                    .getVolumeBetween(today.toEpochSecond() * 1000, now.toEpochSecond() * 1000);

            if (todayVolume == null) {
                todayVolume = 0d;
            }

            Set<String> currencies = allUsedCurrencies();

            Map<String, Double> athPerCorridor = new TreeMap<>();

            if (this.globalStats != null) {
                athPerCorridor.putAll(this.globalStats.getAthPerCorridor());
            }

            Map<String, Map<String, Double>> volumePerCorridor = new TreeMap<>();

            for (int i = daysNbr - 2; i >= 0; i--) {

                String dayString = today.minusDays(i + 1).toString().substring(2, 10);

                if (this.globalStats != null && this.globalStats.getVolumePerCorridor()
                        .containsKey(dayString)) {
                    volumePerCorridor.put(dayString, this.globalStats.getVolumePerCorridor().get(dayString));
                    athPerCorridor.putAll(this.globalStats.getAthPerCorridor());
                    continue;
                }

                for (String source : currencies) {
                    for (String destination : currencies) {

                        if (source.equals(destination)) {
                            continue;
                        }

                        String corridor = source + "-" + destination;

                        Double dayVolume = repository
                                .getVolumeBySourceFiatAndDestinationFiatBetween(source, destination,
                                        today.minusDays(i + 1).toEpochSecond() * 1000,
                                        today.minusDays(i + 1).plusDays(1).toEpochSecond() * 1000);

                        if (dayVolume == null) {
                            dayVolume = 0d;
                        }

                        if (!volumePerCorridor.containsKey(dayString)) {
                            volumePerCorridor.put(dayString, new TreeMap<>());
                        }

                        if ((!athPerCorridor.containsKey(corridor) && dayVolume > 0) || (
                                athPerCorridor.containsKey(corridor) && dayVolume > athPerCorridor.get(corridor))) {
                            athPerCorridor.put(corridor, dayVolume);
                        }

                        if (dayVolume > 0) {
                            volumePerCorridor.get(dayString).put(corridor, dayVolume);
                        }
                    }
                }
            }

            Map<String, Double> todayVolumePerCorridor = new HashMap<>();

            for (String source : currencies) {
                for (String destination : currencies) {

                    if (source.equals(destination)) {
                        continue;
                    }

                    String corridor = source + "-" + destination;

                    Double dayVolume = repository
                            .getVolumeBySourceFiatAndDestinationFiatBetween(source, destination,
                                    today.toEpochSecond() * 1000, now.toEpochSecond() * 1000);

                    if (dayVolume != null && dayVolume > 0) {
                        todayVolumePerCorridor.put(corridor, dayVolume);
                    }
                }
            }

            calculateDailyVolumes(false);

            double athDayVolume = dailyVolumes.values().stream()
                    .mapToDouble(Double::doubleValue)
                    .max().getAsDouble();

            return GlobalStats.builder()
                    .todayVolumePerCorridor(todayVolumePerCorridor)
                    .dailyAth(athDayVolume)
                    .athPerCorridor(athPerCorridor)
                    .totalVolume(roundVolume(allTimeVolume))
                    .todayVolume(roundVolume(todayVolume))
                    .volumePerCorridor(volumePerCorridor)
                    .allTimeFrom(repository.getFirstOdl().getDateTime())
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Scheduled(fixedDelay = 60000)
    public void forceUpdateAths() {
        calculateDailyVolumes(false);
        globalStats = calculateGlobalStats(true);
    }

    private void calculateDailyVolumes(boolean force) {
        OffsetDateTime today = OffsetDateTime.now(ZoneOffset.UTC).withMinute(0).withHour(0)
                .withSecond(0).withNano(0);
        OffsetDateTime day = today.minusDays(1);

        if (dailyVolumes.isEmpty() || force) {
            for (int i = 0; i < 365; i++) {
                Double volume = repository
                        .getVolumeBetween(day.toEpochSecond() * 1000, day.plusDays(1).toEpochSecond() * 1000);
                dailyVolumes.put(day, volume == null ? 0 : volume);
                day = day.minusDays(1);
            }
        } else {
            OffsetDateTime latestCalculatedDay = dailyVolumes.keySet().stream()
                    .max(Comparator.comparing(OffsetDateTime::toEpochSecond)).orElse(null);

            if (latestCalculatedDay != null) {
                day = latestCalculatedDay.plusDays(1);

                while (day.isBefore(today)) {
                    Double volume = repository
                            .getVolumeBetween(day.toEpochSecond() * 1000, day.plusDays(1).toEpochSecond() * 1000);

                    dailyVolumes.put(day, volume == null ? 0 : volume);

                    day = day.plusDays(1);
                }
            }

        }
    }

    @Transactional(readOnly = true)
    public OdlPaymentsResponse search(String begin, String end, Currency source, Currency destination,
                                      Long tag, int pageSize, int page) {

        DateTimeFormatter DATE_FORMAT = new DateTimeFormatterBuilder().appendPattern("dd-MM-yyyy")
                .parseDefaulting(ChronoField.HOUR_OF_DAY, 0)
                .parseDefaulting(ChronoField.MINUTE_OF_HOUR, 0)
                .parseDefaulting(ChronoField.SECOND_OF_MINUTE, 0)
                .parseDefaulting(ChronoField.MILLI_OF_SECOND, 0)
                .parseDefaulting(ChronoField.OFFSET_SECONDS, 0)
                .toFormatter();

        CriteriaBuilder criteriaBuilder = entityManager
                .getCriteriaBuilder();

        CriteriaQuery<ExchangeToExchangePayment> criteriaQuery = criteriaBuilder
                .createQuery(ExchangeToExchangePayment.class);

        Root<ExchangeToExchangePayment> root = criteriaQuery.from(ExchangeToExchangePayment.class);

        List<Predicate> predicates = new ArrayList<>();

        if (destination != null) {
            predicates.add(criteriaBuilder.equal(root.get("destinationFiat"), destination));
        }

        if (source != null) {
            predicates.add(criteriaBuilder.equal(root.get("sourceFiat"), source));
        }

        if (begin != null) {
            predicates.add(criteriaBuilder.ge(root.get("timestamp"), OffsetDateTime.parse(begin,
                    DATE_FORMAT).toEpochSecond() * 1000));
        }

        if (end != null) {
            predicates.add(criteriaBuilder.le(root.get("timestamp"), OffsetDateTime.parse(end,
                    DATE_FORMAT).toEpochSecond() * 1000));
        }

        if (tag != null) {
            predicates.add(criteriaBuilder.equal(root.get("tag"), tag));
        }

        criteriaQuery.where(predicates.toArray(new Predicate[predicates.size()]));

        CriteriaQuery<ExchangeToExchangePayment> select = criteriaQuery.select(root);

        select.orderBy(criteriaBuilder.desc(root.get("dateTime")));

        TypedQuery<ExchangeToExchangePayment> typedQuery = entityManager.createQuery(select);

        CriteriaBuilder qb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> cq = qb.createQuery(Long.class);
        cq.select(qb.count(cq.from(ExchangeToExchangePayment.class)));
        cq.where(predicates.toArray(new Predicate[predicates.size()]));

        Long count = entityManager.createQuery(cq).getSingleResult();

        if (pageSize > 1000) {
            pageSize = 1000;
        }

        if (page <= 0) {
            page = 1;
        }

        int firstResult = (page - 1) * pageSize;

        typedQuery.setFirstResult(firstResult);
        typedQuery.setMaxResults(pageSize);

        List<ExchangeToExchangePayment> payments = typedQuery.getResultList();

        int pages = (int) Math.ceil((double) count / pageSize);
        
        return OdlPaymentsResponse.builder().pages(pages).currentPage(page)
                .pageSize(pageSize).payments(payments).total(count).build();

    }

    private double roundVolume(double volume) {
        return Math.round(volume * 100.0) / 100.0;
    }

    @Cacheable(value = "lastOdlCache", key = "1")
    public List<ExchangeToExchangePayment> getLasts() {
        List<ExchangeToExchangePayment> payments = repository.findTop(350);

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
