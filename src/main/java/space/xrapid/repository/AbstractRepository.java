package space.xrapid.repository;

import org.springframework.scheduling.annotation.Scheduled;
import space.xrapid.domain.Payment;

import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

//TODO migrate to Spring Data
public abstract class AbstractRepository<T extends Payment> {

    private static final int MAX_PAYMENTS_IN_CACHE = 500000;
    private static final int NBR_PAYMENTS_TO_PURGE = MAX_PAYMENTS_IN_CACHE / 2;

    Set<T> data = new HashSet<>();

    public void fill(List<T> payments) {
        data.addAll(payments);
    }

    public void fill(T payment) {
        data.add(payment);
    }

    public List<T> getLasts() {
        return data.stream()
                .sorted(Comparator.comparing(Payment::getTimestamp))
                .limit(1000)
                .collect(Collectors.toList());
    }

    public Set<T> getAll() {
        return data;
    }

    public Set<T> getPaymentsByTimeAndAmount(long timestamp, double amount) {
        throw new UnsupportedOperationException();
    }

    @Scheduled(fixedDelay = 600000)
    public void purge() {
        if (data.size() > MAX_PAYMENTS_IN_CACHE) {
            data = data.stream()
                    .sorted(Comparator.comparing(Payment::getTimestamp))
                    .limit(NBR_PAYMENTS_TO_PURGE).collect(Collectors.toSet());
        }
    }
}
