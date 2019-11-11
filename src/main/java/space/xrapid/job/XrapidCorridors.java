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
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
    protected final double HUGE_TRANSACTION_TOLERANCE = 50;
    protected final double MEDIUM_TRANSACTION_TOLERANCE = 10;
    protected final double SMALL_TRANSACTION_TOLERANCE = 1;

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

    protected List<Trade> takeClosesTrades(ExchangeToExchangePayment exchangeToExchangePayment, List<List<Trade>> groupedXrpTrades) {

        return groupedXrpTrades.stream()
                .sorted(Comparator.comparing(tradesGroup -> getAmountDelta(exchangeToExchangePayment, (List<Trade>) tradesGroup)))
                .findFirst().get();
    }

    protected double getAmountDelta(ExchangeToExchangePayment exchangeToExchangePayment, List<Trade> tradesGroup) {
        return Double.valueOf(Math.abs(exchangeToExchangePayment.getAmount() - totalAmount(tradesGroup)));
    }

    protected double totalAmount(List<Trade> trades) {
        return trades.stream().mapToDouble(Trade::getAmount).sum();
    }

    protected boolean amountMatches(double trxAmount, double aggregatedAmount) {
        double diff = Math.abs(trxAmount - aggregatedAmount);
        return (trxAmount > HUGE_TRANSACTION_THRESHOLD && diff < HUGE_TRANSACTION_TOLERANCE)
                || (trxAmount > MEDIUM_TRANSACTION_THRESHOLD && diff < MEDIUM_TRANSACTION_TOLERANCE)
                || diff < SMALL_TRANSACTION_TOLERANCE;
    }

    protected void persistPayment(ExchangeToExchangePayment exchangeToFiatPayment) {
        exchangeToFiatPayment.setUsdValue(exchangeToFiatPayment.getAmount() * rate);
        exchangeToFiatPayment.setDestinationFiat(exchangeToFiatPayment.getDestination().getLocalFiat());
        exchangeToFiatPayment.setSourceFiat(exchangeToFiatPayment.getSource().getLocalFiat());
        if (exchangeToExchangePaymentService.save(exchangeToFiatPayment, false)) {
            notify(exchangeToFiatPayment);
        }
    }

    protected void notify(ExchangeToExchangePayment payment) {
        log.info("Xrapid payment {} ", payment);
        messagingTemplate.convertAndSend("/topic/payments", payment);
    }

    protected boolean xrpToFiatTradesExists(ExchangeToExchangePayment exchangeToExchangePayment) {
        exchangeToExchangePayment.setDestinationCurrencry(exchangeToExchangePayment.getDestinationFiat());

        List<List<Trade>> candidates = new ArrayList<>();

        Stream.of("buy", "sell").forEach(side -> {
            findCandidates((ArrayList<Trade>) trades.stream()
                    .filter(trade -> getDestinationExchange().equals(exchangeToExchangePayment.getDestination()))
                    .filter(trade -> trade.getExchange().equals(getDestinationExchange()))
                    .filter(trade -> (trade.getDateTime().toEpochSecond() - exchangeToExchangePayment.getDateTime().toEpochSecond()) >= 1)
                    .filter(trade -> (trade.getDateTime().toEpochSecond() - exchangeToExchangePayment.getDateTime().toEpochSecond()) < 90)
                    .filter(trade -> !tradesIdAlreadyProcessed.contains(trade.getOrderId()))
                    .collect(Collectors.toList()), exchangeToExchangePayment.getAmount(), candidates, side);
        });

        if (!candidates.isEmpty()) {

            List<Trade> trades = takeClosesTrades(exchangeToExchangePayment, candidates);

            exchangeToExchangePayment.setXrpToFiatTrades(trades);

            String tradeIds = trades.stream().map(Trade::getOrderId).collect(Collectors.joining(";"));
            exchangeToExchangePayment.setInTradeFound(true);
            exchangeToExchangePayment.setTradeIds(tradeIds);

            tradesIdAlreadyProcessed.addAll(trades.stream().map(Trade::getOrderId).collect(Collectors.toList()));

            return true;
        }

        return false;
    }

    protected boolean fiatToXrpTradesExists(ExchangeToExchangePayment exchangeToExchangePayment) {
        if ("4F5CDD3FDD807B6FD899A6B3A778FC4CBED2A4357EA58774C0A9AED637E019D7".equals(exchangeToExchangePayment.getTransactionHash())) {
            System.out.println("Debug");
        }
        exchangeToExchangePayment.setDestinationCurrencry(exchangeToExchangePayment.getDestinationFiat());

        List<List<Trade>> candidates = new ArrayList<>();

        try {
            Stream.of("buy", "sell").forEach(side -> {
                findCandidates((ArrayList<Trade>) trades.stream()
                        .filter(trade -> trade.getOrderId() != null)
                        .filter(trade -> side.equals(trade.getSide()))
                        .filter(trade -> getDestinationExchange().equals(exchangeToExchangePayment.getDestination()))
                        .filter(trade -> trade.getExchange().equals(exchangeToExchangePayment.getSource()))
                        .filter(trade -> (exchangeToExchangePayment.getDateTime().toEpochSecond() - trade.getDateTime().toEpochSecond()) >= 1)
                        .filter(trade -> (exchangeToExchangePayment.getDateTime().toEpochSecond() - trade.getDateTime().toEpochSecond()) < 90)
                        .filter(trade -> !tradesIdAlreadyProcessed.contains(trade.getOrderId()))
                        .collect(Collectors.toList()), exchangeToExchangePayment.getAmount(), candidates, side);
            });
        } catch (Exception e) {
            e.printStackTrace();
        }


        if (!candidates.isEmpty()) {

            List<Trade> trades = takeClosesTrades(exchangeToExchangePayment, candidates);

            exchangeToExchangePayment.setFiatToXrpTrades(trades);

            String tradeIds = trades.stream().map(Trade::getOrderId).collect(Collectors.joining(";"));

            exchangeToExchangePayment.setOutTradeFound(true);
            exchangeToExchangePayment.setTradeOutIds(tradeIds);

            tradesIdAlreadyProcessed.addAll(trades.stream().map(Trade::getOrderId).collect(Collectors.toList()));

            return true;
        }

        return false;
    }

    protected void submit(List<Payment> payments) {
        List<Payment> paymentsToProcess = payments.stream()
                .filter(this::isXrapidCandidate).collect(Collectors.toList());

        if (paymentsToProcess.isEmpty()) {
            return;
        }

        paymentsToProcess.stream()
                .map(this::mapPayment)
                .filter(this::xrpToFiatTradesExists)
                .sorted(Comparator.comparing(ExchangeToExchangePayment::getDateTime))
                .forEach(this::persistPayment);
    }

    protected abstract SpottedAt getSpottedAt();

    void recursiveFindCandidates(ArrayList<Trade> trades, double trxAmount, ArrayList<Trade> partial, List<List<Trade>> candidates, String side) {

        double sum = partial.stream().mapToDouble(Trade::getAmount).sum();

        if (Math.abs(sum - trxAmount) < 1) {
            candidates.add(partial);
        }


        if (sum >= trxAmount) {
            return;
        }

        for (int i = 0; i < trades.size(); i++) {
            ArrayList<Trade> remaining = new ArrayList<>();
            for (int j = i + 1; j < trades.size(); j++) remaining.add(trades.get(j));
            ArrayList<Trade> partial_rec = new ArrayList<>(partial);
          //  if (trades.get(i).getSide().equals(side)) {
                partial_rec.add(trades.get(i));
           // }
            recursiveFindCandidates(remaining, trxAmount, partial_rec, candidates, side);
        }
    }

    void findCandidates(ArrayList<Trade> trades, double trxAmount, List<List<Trade>> candidates, String side) {
        recursiveFindCandidates(trades, trxAmount, new ArrayList<>(), candidates, side);
    }

}
