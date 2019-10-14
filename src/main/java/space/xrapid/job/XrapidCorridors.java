package space.xrapid.job;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.scheduling.annotation.Scheduled;
import space.xrapid.domain.Exchange;
import space.xrapid.domain.ExchangeToExchangePayment;
import space.xrapid.domain.XrpTrade;
import space.xrapid.domain.ripple.Payment;
import space.xrapid.repository.XrapidPaymentRepository;
import space.xrapid.service.TradeService;
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
public abstract class XrapidCorridors {

    @Autowired
    private XrapidPaymentRepository xrapidPaymentRepository;

    @Autowired
    private SimpMessageSendingOperations messagingTemplate;

    @Autowired
    private XrpLedgerService xrpLedgerService;

    private OffsetDateTime lastWindowEnd;
    private OffsetDateTime windowStart;
    private OffsetDateTime windowEnd;

    private List<XrpTrade> xrpTrades = new ArrayList<>();

    private DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss'Z'");

    private List<String> allExchangeAddresses;

    @PostConstruct
    public void init() {
        allExchangeAddresses = Arrays.stream(Exchange.values()).map(e -> e.getAddresses()).flatMap(Arrays::stream)
                .collect(Collectors.toList());
    }

    @Scheduled(fixedDelay = 30000)
    public void updateExchangeToExchangePayments() {
        updatePaymentsWindows();

        xrpTrades = getTradeService().fetchTrades(windowStart);

        xrpLedgerService.fetchPayments(windowStart.plusMinutes(-5), windowEnd, this::submit);
    }

    private void notify(ExchangeToExchangePayment payment) {
        log.info("Xrapid payment {} ", payment);
        messagingTemplate.convertAndSend("/topic/payments", payment);
    }

    private boolean isXrapidCandidate(Payment payment) {
        return allExchangeAddresses.contains(payment.getDestination()) && allExchangeAddresses.contains(payment.getSource());
    }

    private void submit(List<Payment> payments) {
        List<Payment> paymentsToProcess =  payments.stream()
                .filter(this::isXrapidCandidate).collect(Collectors.toList());

        if (paymentsToProcess.isEmpty()) {
            return;
        }

        paymentsToProcess.stream()
                .map(this::mapPayment)
                .filter(this::mxnXrpToCurrencyTradeExist)
                .sorted(Comparator.comparing(ExchangeToExchangePayment::getTimestamp))
                .peek(System.out::println)
                .peek(this::notify)
                .peek(xrapidPaymentRepository::fill)
                .collect(Collectors.toList());
    }

    private boolean mxnXrpToCurrencyTradeExist(ExchangeToExchangePayment exchangeToExchangePayment) {
        return xrpTrades.stream()
                .filter(p -> Exchange.BITSO.equals(exchangeToExchangePayment.getDestination()))
                .filter(p -> exchangeToExchangePayment.getAmount().equals(p.getAmount()))
                .findAny().isPresent();
    }

    private ExchangeToExchangePayment mapPayment(Payment payment) {
        try {
            return ExchangeToExchangePayment.builder()
                    .amount(Double.valueOf(payment.getDeliveredAmount()))
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
        windowStart = windowEnd.plusMinutes(-800);

        if (lastWindowEnd != null) {
            windowStart = lastWindowEnd;
        }

        lastWindowEnd = windowEnd;
    }

    protected abstract TradeService getTradeService();
}
