package space.xrapid.job;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import space.xrapid.domain.Exchange;
import space.xrapid.domain.ExchangeToExchangePayment;
import space.xrapid.domain.XrpTrade;
import space.xrapid.domain.ripple.Payment;
import space.xrapid.service.ExchangeToExchangePaymentService;
import space.xrapid.service.TradeService;
import space.xrapid.service.XrapidInboundAddressService;

import javax.annotation.PostConstruct;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
public abstract class XrapidCorridors {

    @Autowired
    private ExchangeToExchangePaymentService exchangeToExchangePaymentService;

    @Autowired
    private SimpMessageSendingOperations messagingTemplate;

    private List<XrpTrade> xrpTrades = new ArrayList<>();

    private DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss'Z'");

    private List<String> allExchangeAddresses;

    private final double HUGE_TRANSACTION_THRESHOLD = 30000;
    private final double MEDIUM_TRANSACTION_THRESHOLD = 5000;

    private final double HUGE_TRANSACTION_TOLERANCE = 200;
    private final double MEDIUM_TRANSACTION_TOLERANCE = 5;
    private final double SMALL_TRANSACTION_TOLERANCE = 1;


    @PostConstruct
    public void init() {
        allExchangeAddresses = Arrays.stream(Exchange.values()).map(e -> e.getAddresses()).flatMap(Arrays::stream)
                .collect(Collectors.toList());
    }

    public void searchXrapidPayments(List<Payment> payments, OffsetDateTime windowStart) {

        if (getTradeService() != null) {
            xrpTrades = getTradeService().fetchTrades(windowStart);
        }

        submit(payments);
    }

    private void notify(ExchangeToExchangePayment payment) {
        log.info("Xrapid payment {} ", payment);
        messagingTemplate.convertAndSend("/topic/payments", payment);
    }

    private boolean isXrapidCandidate(Payment payment) {
        return getDestinationExchange().equals(Exchange.byAddress(payment.getDestination())) && allExchangeAddresses.contains(payment.getSource());
    }

    private void submit(List<Payment> payments) {
        List<Payment> paymentsToProcess = payments.stream()
                .filter(this::isXrapidCandidate).collect(Collectors.toList());

        if (paymentsToProcess.isEmpty()) {
            return;
        }

        paymentsToProcess.stream()
                .map(this::mapPayment)
                .filter(this::xrpToFiatTradeExistOrAddressIdentified)
                .sorted(Comparator.comparing(ExchangeToExchangePayment::getDateTime))
                .forEach(this::persistPayment);

    }

    private void persistPayment(ExchangeToExchangePayment exchangeToFiatPayment) {
        if (exchangeToExchangePaymentService.save(exchangeToFiatPayment)) {
            notify(exchangeToFiatPayment);
        }
    }

    private boolean xrpToFiatTradeExistOrAddressIdentified(ExchangeToExchangePayment exchangeToExchangePayment) {
        exchangeToExchangePayment.setDestinationCurrencry(exchangeToExchangePayment.getDestination().getLocalFiat());

        Map<OffsetDateTime, List<XrpTrade>> aggregatedTrades  = xrpTrades.stream()
                .filter(trade -> getDestinationExchange().equals(exchangeToExchangePayment.getDestination()))
                .filter(trade -> (trade.getDateTime().toEpochSecond() - exchangeToExchangePayment.getDateTime().toEpochSecond()) >= 0)
                .filter(trade -> (trade.getDateTime().toEpochSecond() - exchangeToExchangePayment.getDateTime().toEpochSecond()) < 60)
                .collect(Collectors.groupingBy(XrpTrade::getDateTime));


        List<List<XrpTrade>> candidates = new ArrayList<>();

        for (Map.Entry<OffsetDateTime, List<XrpTrade>> e : aggregatedTrades.entrySet()) {
            double amount = e.getValue().stream().mapToDouble(XrpTrade::getAmount).sum();

            if (amountMatches(exchangeToExchangePayment, amount)) {
                candidates.add(e.getValue());
            }
        }

        if (!candidates.isEmpty()) {

            List<XrpTrade> xrpTrades = takeClosest(exchangeToExchangePayment, candidates);

            exchangeToExchangePayment.setToFiatTrades(xrpTrades);
            exchangeToExchangePayment.setTradeIds(xrpTrades.stream().map(XrpTrade::getOrderId).collect(Collectors.joining(";")));

            return true;
        }

        return false;
    }


    private List<XrpTrade> takeClosest(ExchangeToExchangePayment exchangeToExchangePayment, List<List<XrpTrade>> groupedXrpTrades) {

        return groupedXrpTrades.stream()
                .sorted(Comparator.comparing(tradesGroup -> getAmountDelta(exchangeToExchangePayment, (List<XrpTrade>) tradesGroup))
                        .thenComparing(tradesGroup -> getDateDelta(exchangeToExchangePayment, (List<XrpTrade>) tradesGroup)))
                .findFirst().get();
    }

    private double getDateDelta(ExchangeToExchangePayment exchangeToExchangePayment, List<XrpTrade> tradesGroup) {
        return Double.valueOf(tradesGroup.get(0).getTimestamp() - exchangeToExchangePayment.getTimestamp());
    }

    private double getAmountDelta(ExchangeToExchangePayment exchangeToExchangePayment, List<XrpTrade> tradesGroup) {
        return Double.valueOf(Math.abs(exchangeToExchangePayment.getAmount() - totalAmount(tradesGroup)));
    }

    private double totalAmount(List<XrpTrade> trades) {
        return trades.stream().mapToDouble(XrpTrade::getAmount).sum();
    }

    private boolean amountMatches(ExchangeToExchangePayment exchangeToExchangePayment, double aggregatedAmount) {
        return (exchangeToExchangePayment.getAmount() > HUGE_TRANSACTION_THRESHOLD && Math.abs(exchangeToExchangePayment.getAmount() - aggregatedAmount) < HUGE_TRANSACTION_TOLERANCE)
                || (exchangeToExchangePayment.getAmount() > MEDIUM_TRANSACTION_THRESHOLD && Math.abs(exchangeToExchangePayment.getAmount() - aggregatedAmount) < MEDIUM_TRANSACTION_TOLERANCE)
                ||  Math.abs(exchangeToExchangePayment.getAmount() - aggregatedAmount) < SMALL_TRANSACTION_TOLERANCE;
    }

    private ExchangeToExchangePayment mapPayment(Payment payment) {
        try {

            Exchange source = Exchange.byAddress(payment.getSource());
            Exchange destination = Exchange.byAddress(payment.getDestination());
            boolean xrapidCorridorConfirmed = source.isConfirmed() && destination.isConfirmed();

            return ExchangeToExchangePayment.builder()
                    .amount(Double.valueOf(payment.getDeliveredAmount()))
                    .destination(Exchange.byAddress(payment.getDestination()))
                    .source(Exchange.byAddress(payment.getSource()))
                    .sourceAddress(payment.getSource())
                    .destinationAddress(payment.getDestination())
                    .tag(payment.getDestinationTag())
                    .transactionHash(payment.getTxHash())
                    .timestamp(dateFormat.parse(payment.getExecutedTime()).getTime())
                    .dateTime(OffsetDateTime.parse(payment.getExecutedTime(), DateTimeFormatter.ISO_OFFSET_DATE_TIME))
                    .confirmed(xrapidCorridorConfirmed)
                    .build();
        } catch (ParseException e) {
            return null;
        }
    }

    protected abstract TradeService getTradeService();

    protected abstract Exchange getDestinationExchange();

    protected abstract int getPriority();

}
