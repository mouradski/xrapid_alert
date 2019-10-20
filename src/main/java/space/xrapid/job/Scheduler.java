package space.xrapid.job;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import space.xrapid.domain.ripple.Payment;
import space.xrapid.service.XrpLedgerService;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Comparator;
import java.util.List;

@Slf4j
@EnableScheduling
@Component
public class Scheduler {

    @Autowired
    private List<XrapidCorridors> corridors;

    @Autowired
    private XrpLedgerService xrpLedgerService;

    private OffsetDateTime lastWindowEnd;
    private OffsetDateTime windowStart;
    private OffsetDateTime windowEnd;

    @Scheduled(fixedDelay = 30000)
    public void process() {
        updatePaymentsWindows();

        List<Payment> payments = xrpLedgerService.fetchPayments(windowStart.plusMinutes(-5), windowEnd);

        corridors.stream()
                .sorted(Comparator.comparing(XrapidCorridors::getPriority))
                .forEach(c -> c.searchXrapidPayments(payments, windowStart));
    }

    private void updatePaymentsWindows() {
        windowEnd = OffsetDateTime.now(ZoneOffset.UTC);
        windowStart = windowEnd.plusMinutes(-70);

        if (lastWindowEnd != null) {
            windowStart = lastWindowEnd;
        }

        lastWindowEnd = windowEnd;
    }
}
