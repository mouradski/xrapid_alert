package space.xrapid.job;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import space.xrapid.domain.Exchange;
import space.xrapid.domain.ExchangeToExchangePayment;
import space.xrapid.domain.SpottedAt;
import space.xrapid.domain.Trade;
import space.xrapid.domain.ripple.Payment;
import space.xrapid.service.ExchangeToExchangePaymentService;
import space.xrapid.service.TradeCacheService;

import javax.annotation.PostConstruct;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
public abstract class XrapidCorridors {

    @Autowired
    protected ExchangeToExchangePaymentService exchangeToExchangePaymentService;

    @Autowired
    protected SimpMessageSendingOperations messagingTemplate;

    @Autowired
    protected TradeCacheService tradeCacheService;

    protected List<Trade> trades = new ArrayList<>();


    protected List<String> allExchangeAddresses;
    protected Set<String> tradesIdAlreadyProcessed = new HashSet<>();

    protected final double HUGE_TRANSACTION_THRESHOLD = 30000;
    protected final double MEDIUM_TRANSACTION_THRESHOLD = 5000;
    protected final double HUGE_TRANSACTION_TOLERANCE = 200;
    protected final double MEDIUM_TRANSACTION_TOLERANCE = 5;
    protected final double SMALL_TRANSACTION_TOLERANCE = 0.1;

    protected double rate;

    @PostConstruct
    public void init() {
        allExchangeAddresses = Arrays.stream(Exchange.values()).map(e -> e.getAddresses()).flatMap(Arrays::stream)
                .collect(Collectors.toList());
    }

    protected abstract Exchange getDestinationExchange();

    protected ExchangeToExchangePayment mapPayment(Payment payment) {
        try {
            Exchange source = Exchange.byAddress(payment.getSource());
            Exchange destination = Exchange.byAddress(payment.getDestination());
            boolean xrapidCorridorConfirmed = source.isConfirmed() && destination.isConfirmed();

            OffsetDateTime dateTime = OffsetDateTime.parse(payment.getExecutedTime(), DateTimeFormatter.ISO_OFFSET_DATE_TIME);

            return ExchangeToExchangePayment.builder()
                    .amount(Double.valueOf(payment.getDeliveredAmount()))
                    .destination(Exchange.byAddress(payment.getDestination()))
                    .source(Exchange.byAddress(payment.getSource()))
                    .sourceAddress(payment.getSource())
                    .destinationAddress(payment.getDestination())
                    .tag(payment.getDestinationTag())
                    .transactionHash(payment.getTxHash())
                    .timestamp(dateTime.toEpochSecond() * 1000)
                    .dateTime(dateTime)
                    .confirmed(xrapidCorridorConfirmed)
                    .spottedAt(getSpottedAt())
                    .build();
        } catch (Exception e) {
            return null;
        }
    }

    protected boolean isXrapidCandidate(Payment payment) {
        return Double.valueOf(payment.getAmount()) > 10 && getDestinationExchange().equals(Exchange.byAddress(payment.getDestination())) && allExchangeAddresses.contains(payment.getSource());
    }

    protected List<Trade> takeClosest(ExchangeToExchangePayment exchangeToExchangePayment, List<List<Trade>> groupedXrpTrades) {

        return groupedXrpTrades.stream()
                .sorted(Comparator.comparing(tradesGroup -> getAmountDelta(exchangeToExchangePayment, (List<Trade>) tradesGroup))
                        .thenComparing(tradesGroup -> getDateDelta(exchangeToExchangePayment, (List<Trade>) tradesGroup)))
                .findFirst().get();
    }

    protected double getDateDelta(ExchangeToExchangePayment exchangeToExchangePayment, List<Trade> tradesGroup) {
        return Double.valueOf(tradesGroup.get(0).getTimestamp() - exchangeToExchangePayment.getTimestamp());
    }

    protected double getAmountDelta(ExchangeToExchangePayment exchangeToExchangePayment, List<Trade> tradesGroup) {
        return Double.valueOf(Math.abs(exchangeToExchangePayment.getAmount() - totalAmount(tradesGroup)));
    }

    protected double totalAmount(List<Trade> trades) {
        return trades.stream().mapToDouble(Trade::getAmount).sum();
    }

    protected boolean amountMatches(ExchangeToExchangePayment exchangeToExchangePayment, double aggregatedAmount) {
        return (exchangeToExchangePayment.getAmount() > HUGE_TRANSACTION_THRESHOLD && Math.abs(exchangeToExchangePayment.getAmount() - aggregatedAmount) < HUGE_TRANSACTION_TOLERANCE)
                || (exchangeToExchangePayment.getAmount() > MEDIUM_TRANSACTION_THRESHOLD && Math.abs(exchangeToExchangePayment.getAmount() - aggregatedAmount) < MEDIUM_TRANSACTION_TOLERANCE)
                || Math.abs(exchangeToExchangePayment.getAmount() - aggregatedAmount) < SMALL_TRANSACTION_TOLERANCE;
    }

    protected void persistPayment(ExchangeToExchangePayment exchangeToFiatPayment) {
        exchangeToFiatPayment.setUsdValue(exchangeToFiatPayment.getAmount() * rate);
        if (exchangeToExchangePaymentService.save(exchangeToFiatPayment)) {
            notify(exchangeToFiatPayment);
        }
    }

    protected void notify(ExchangeToExchangePayment payment) {
        log.info("Xrapid payment {} ", payment);
        messagingTemplate.convertAndSend("/topic/payments", payment);
    }

    protected boolean tradeExists(ExchangeToExchangePayment exchangeToExchangePayment) {
        exchangeToExchangePayment.setDestinationCurrencry(exchangeToExchangePayment.getDestination().getLocalFiat());

        Map<String, List<Trade>> aggregatedTrades = getAggregatedTrades(exchangeToExchangePayment);


        List<List<Trade>> candidates = new ArrayList<>();

        for (Map.Entry<String, List<Trade>> e : aggregatedTrades.entrySet()) {
            double amount = e.getValue().stream().mapToDouble(Trade::getAmount).sum();

            if (amountMatches(exchangeToExchangePayment, amount)) {
                candidates.add(e.getValue());
            }
        }

        if (!candidates.isEmpty()) {

            List<Trade> trades = takeClosest(exchangeToExchangePayment, candidates);

            exchangeToExchangePayment.setToFiatTrades(trades);

            String tradeIds = trades.stream().map(Trade::getOrderId).collect(Collectors.joining(";"));

            exchangeToExchangePayment.setTradeIds(tradeIds);

            tradesIdAlreadyProcessed.addAll(trades.stream().map(Trade::getOrderId).collect(Collectors.toList()));

            return true;
        }

        return false;
    }

    protected Map<String, List<Trade>> getAggregatedTrades(ExchangeToExchangePayment exchangeToExchangePayment) {
        return trades.stream()
                .filter(trade -> getDestinationExchange().equals(exchangeToExchangePayment.getDestination()))
                .filter(trade -> (trade.getDateTime().toEpochSecond() - exchangeToExchangePayment.getDateTime().toEpochSecond()) >= 0)
                .filter(trade -> (trade.getDateTime().toEpochSecond() - exchangeToExchangePayment.getDateTime().toEpochSecond()) < 90)
                .filter(trade -> !tradesIdAlreadyProcessed.contains(trade.getOrderId()))
                .collect(Collectors.groupingBy(Trade::getDateTimeAndOrderSide));
    }

    protected void submit(List<Payment> payments) {
        List<Payment> paymentsToProcess = payments.stream()
                .filter(this::isXrapidCandidate).collect(Collectors.toList());

        if (paymentsToProcess.isEmpty()) {
            return;
        }

        paymentsToProcess.stream()
                .map(this::mapPayment)
                .filter(this::tradeExists)
                .sorted(Comparator.comparing(ExchangeToExchangePayment::getDateTime))
                .forEach(this::persistPayment);
    }

    protected abstract SpottedAt getSpottedAt();

}
