package space.xrapid.job;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import space.xrapid.domain.Exchange;
import space.xrapid.domain.ExchangeToExchangePayment;
import space.xrapid.domain.bitso.Bid;
import space.xrapid.domain.ripple.Payment;
import space.xrapid.repository.XrapidPaymentRepository;
import space.xrapid.service.BitsoService;
import space.xrapid.service.XrpLedgerService;

import javax.annotation.PostConstruct;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@EnableScheduling
@Component
public class MxnCorridor {

    @Autowired
    private XrapidPaymentRepository xrapidPaymentRepository;

    @Autowired
    private SimpMessageSendingOperations messagingTemplate;

    @Autowired
    private XrpLedgerService xrpLedgerService;

    @Autowired
    private BitsoService bitsoService;

    private OffsetDateTime lastWindowEnd;
    private OffsetDateTime windowStart;
    private OffsetDateTime windowEnd;

    private DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss'Z'");

    private List<String> allExchangeAddresses;

    private List<Bid> bitsoBids = new ArrayList<>();

    @PostConstruct
    public void init() {
        allExchangeAddresses = Arrays.stream(Exchange.values()).map(e -> e.getAddresses()).flatMap(Arrays::stream)
                .collect(Collectors.toList());
    }

    @Scheduled(fixedDelay = 5000)
    public void updateExchangeToExchangePayments() {
        updatePaymentsWindows();
        bitsoBids = bitsoService.fetchPayments();
        xrpLedgerService.fetchPayments(windowStart.plusMinutes(-2), windowEnd, this::submit);
    }

    private void notify(ExchangeToExchangePayment payment) {
        messagingTemplate.convertAndSend("/topic/payments", payment);
    }

    private boolean isXrapidCandidate(Payment payment) {
        return !payment.getDestination().equals(payment.getSource())
                && allExchangeAddresses.contains(payment.getDestination()) && allExchangeAddresses.contains(payment.getSource());
    }

    private void submit(List<Payment> payments) {
        List<Payment> paymentsToProcess =  payments.stream()
                .filter(this::isXrapidCandidate).collect(Collectors.toList());

        if (paymentsToProcess.isEmpty()) {
            return;
        }

        paymentsToProcess.stream()
                .map(this::mapPayment)
                .filter(this::mxnXrpToMxnBidExist)
                .sorted(Comparator.comparing(ExchangeToExchangePayment::getTimestamp))
                .peek(this::notify)
                .peek(xrapidPaymentRepository::fill)
                .collect(Collectors.toList());
    }

    private boolean mxnXrpToMxnBidExist(ExchangeToExchangePayment exchangeToExchangePayment) {
        return bitsoBids.stream()
                .filter(p -> Exchange.BITSO.equals(exchangeToExchangePayment.getDestination()))
                .filter(p -> amountEquals(exchangeToExchangePayment.getAmount(), Double.valueOf(p.getAmount())))
                .findAny().isPresent();
    }

    private boolean amountEquals(double a1, double a2) {
        double diff = Math.abs(a1-a2);
        //trx fee included in ammount ????
        return diff < 0.5;
    }

    private ExchangeToExchangePayment mapPayment(Payment payment) {
        try {
            return ExchangeToExchangePayment.builder()
                    .amount(Double.valueOf(payment.getAmount()))
                    .destination(Exchange.byAddress(payment.getDestination()))
                    .source(Exchange.byAddress(payment.getSource()))
                    .transactionHash(payment.getTxHash())
                    .timestamp(dateFormat.parse(payment.getExecutedTime()).getTime())
                    .build();
        } catch (ParseException e) {
            return null;
        }
    }

    private void updatePaymentsWindows() {
        windowEnd = OffsetDateTime.now(ZoneOffset.UTC);
        windowStart = windowEnd.plusMinutes(-3);

        if (lastWindowEnd != null) {
            windowStart = lastWindowEnd;
        }

        lastWindowEnd = windowEnd;
    }
}
