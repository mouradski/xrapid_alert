package space.xrapid.job;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import space.xrapid.domain.ripple.Payment;
import space.xrapid.service.TradeCacheService;
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
    private List<InboundXrapidCorridors> inboundCorridors;

    @Autowired
    private List<OutboundXrapidCorridors> outboundCorridors;

    @Autowired
    private TradeCacheService tradeCacheService;

    @Autowired
    private XrpLedgerService xrpLedgerService;

    private OffsetDateTime lastWindowEnd;
    private OffsetDateTime windowStart;
    private OffsetDateTime windowEnd;

    @Scheduled(fixedDelay = 30000)
    public void process() {

        tradeCacheService.reset();

        updatePaymentsWindows();

        List<Payment> payments = xrpLedgerService.fetchPayments(windowStart.plusMinutes(-5), windowEnd);

        inboundCorridors.stream()
                .sorted(Comparator.comparing(InboundXrapidCorridors::getPriority))
                .forEach(c -> c.searchXrapidPayments(payments, windowStart));

        outboundCorridors.forEach(c -> {
            c.searchXrapidPayments(payments);
        });
    }

    private void updatePaymentsWindows() {
        windowEnd = OffsetDateTime.now(ZoneOffset.UTC);
        windowStart = windowEnd.plusMinutes(-1400);

        if (lastWindowEnd != null) {
            windowStart = lastWindowEnd;
        }

        lastWindowEnd = windowEnd;
    }
}
