package space.xrapid.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import space.xrapid.domain.Exchange;
import space.xrapid.domain.ExchangeToExchangePayment;
import space.xrapid.domain.SpottedAt;
import space.xrapid.domain.Trade;
import space.xrapid.domain.ripple.Payment;
import space.xrapid.service.ExchangeToExchangePaymentService;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
public abstract class XrapidCorridors {

    protected List<Trade> trades = new ArrayList<>();

    protected List<String> allExchangeAddresses;
    protected Set<String> tradesIdAlreadyProcessed = new HashSet<>();

    protected double rate;

    protected ExchangeToExchangePaymentService exchangeToExchangePaymentService;

    protected List<Exchange> exchangesToExclude;

    protected SimpMessageSendingOperations messagingTemplate;

    public XrapidCorridors(ExchangeToExchangePaymentService exchangeToExchangePaymentService, SimpMessageSendingOperations messagingTemplate, List<Exchange> exchangesToExclude) {

        if (exchangesToExclude == null) {
            this.exchangesToExclude = new ArrayList<>();
        } else {
            this.exchangesToExclude = exchangesToExclude;
        }
        this.exchangeToExchangePaymentService = exchangeToExchangePaymentService;
        this.messagingTemplate = messagingTemplate;
        allExchangeAddresses = Arrays.stream(Exchange.values()).map(e -> e.getAddresses()).flatMap(Arrays::stream)
                .collect(Collectors.toList());
    }

    public abstract Exchange getDestinationExchange();

    public abstract SpottedAt getSpottedAt();

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
        try {
            return Double.valueOf(payment.getAmount()) > 10 && getDestinationExchange().equals(Exchange.byAddress(payment.getDestination())) && allExchangeAddresses.contains(payment.getSource());

        } catch (Exception e) {
            return false;
        }
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

    protected void persistPayment(ExchangeToExchangePayment exchangeToFiatPayment) {
        try  {
            exchangeToFiatPayment.setUsdValue(exchangeToFiatPayment.getAmount() * rate);

            if (exchangeToFiatPayment.getFiatToXrpTrades() != null) {
                exchangeToFiatPayment.setSourceFiat(exchangeToFiatPayment.getFiatToXrpTrades().get(0).getExchange().getLocalFiat());
            } else {
                exchangeToFiatPayment.setSourceFiat(exchangeToFiatPayment.getSource().getLocalFiat());
            }

            if (exchangeToFiatPayment.getXrpToFiatTrades() != null) {
                exchangeToFiatPayment.setDestinationFiat(exchangeToFiatPayment.getXrpToFiatTrades().get(0).getExchange().getLocalFiat());
            } else {
                exchangeToFiatPayment.setDestinationFiat(exchangeToFiatPayment.getDestination().getLocalFiat());
            }

            if (exchangeToFiatPayment.getDestinationFiat() != null &&
                    exchangeToFiatPayment.getDestinationFiat().equals(exchangeToFiatPayment.getSourceFiat())) {
                return;
            }

            if (exchangeToExchangePaymentService.save(exchangeToFiatPayment)) {
                notify(exchangeToFiatPayment);
            }
        } catch (Throwable e) {
            log.error("Erreur persisting {}", exchangeToFiatPayment);
        }

    }

    protected void notify(ExchangeToExchangePayment payment) {
        log.info("Xrapid payment {} ", payment);
        messagingTemplate.convertAndSend("/topic/payments", payment);
    }

    protected boolean xrpToFiatTradesExists(ExchangeToExchangePayment exchangeToExchangePayment) {

        if (exchangesToExclude.contains(exchangeToExchangePayment.getDestination()) && exchangesToExclude.contains(exchangeToExchangePayment.getSource())) {
            return false;
        }

        exchangeToExchangePayment.setDestinationCurrencry(exchangeToExchangePayment.getDestinationFiat());

        List<List<Trade>> candidates = new ArrayList<>();

        Stream.of("buy", "sell").forEach(side -> {
            findCandidates((ArrayList<Trade>) trades.stream()
                    .filter(trade -> trade.getOrderId() != null)
                    .filter(trade -> side.equals(trade.getSide()))
                    .filter(trade -> getDestinationExchange().equals(exchangeToExchangePayment.getDestination()))
                    .filter(trade -> trade.getExchange().equals(getDestinationExchange()))
                    .filter(filterXrpToFiatTradePerDate(exchangeToExchangePayment))
                    .filter(trade -> !tradesIdAlreadyProcessed.contains(trade.getOrderId()))
                    .collect(Collectors.toList()), exchangeToExchangePayment.getAmount(), candidates);
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

    private Predicate<Trade> filterFiatToXrpTradePerDate(ExchangeToExchangePayment exchangeToExchangePayment) {
        return trade -> {
            double diff = exchangeToExchangePayment.getDateTime().toEpochSecond() - trade.getDateTime().toEpochSecond();
            return diff >= 1 && diff < 60;
        };
    }

    private Predicate<Trade> filterXrpToFiatTradePerDate(ExchangeToExchangePayment exchangeToExchangePayment) {
        return trade -> {
            double diff = trade.getDateTime().toEpochSecond() - exchangeToExchangePayment.getDateTime().toEpochSecond();
            return diff >= 1 && diff < 60;
        };
    }

    protected boolean fiatToXrpTradesExists(ExchangeToExchangePayment exchangeToExchangePayment) {

        if (exchangesToExclude.contains(exchangeToExchangePayment.getDestination()) && exchangesToExclude.contains(exchangeToExchangePayment.getSource())) {
            return false;
        }
        exchangeToExchangePayment.setDestinationCurrencry(exchangeToExchangePayment.getDestinationFiat());

        List<List<Trade>> candidates = new ArrayList<>();


        Stream.of("buy", "sell").forEach(side -> {
            try {
                findCandidates((ArrayList<Trade>) trades.stream()
                        .filter(trade -> trade.getOrderId() != null)
                        .filter(trade -> side.equals(trade.getSide()))
                        .filter(trade -> trade.getExchange().equals(exchangeToExchangePayment.getSource()))
                        .filter(filterFiatToXrpTradePerDate(exchangeToExchangePayment))
                        .filter(trade -> !tradesIdAlreadyProcessed.contains(trade.getOrderId()))
                        .collect(Collectors.toList()), exchangeToExchangePayment.getAmount(), candidates);
            } catch (Throwable e) {
                log.error("Error where trying to find trades candidates for TRX {}", exchangeToExchangePayment);
            }
        });


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

    protected List<ExchangeToExchangePayment> submit(List<Payment> payments) {
        List<Payment> paymentsToProcess = payments.stream()
                .filter(this::isXrapidCandidate).collect(Collectors.toList());

        if (paymentsToProcess.isEmpty()) {
            return new ArrayList<>();
        }

        return paymentsToProcess.stream()
                .map(this::mapPayment)
                .filter(this::xrpToFiatTradesExists)
                .sorted(Comparator.comparing(ExchangeToExchangePayment::getDateTime))
                .peek(this::persistPayment)
                .collect(Collectors.toList());
    }

    void recursiveFindCandidates(ArrayList<Trade> trades, double trxAmount, ArrayList<Trade> partial, List<List<Trade>> candidates) {

        double sum = partial.stream().mapToDouble(Trade::getAmount).sum();

        double tolerence = 1;

        if (trxAmount > 1000) {
            tolerence = 5;
        } if (trxAmount > 40000) {
            tolerence = 200;
        }
        if (Math.abs(sum - trxAmount) < tolerence) {
            candidates.add(partial);
        }
        
        if (sum >= trxAmount) {
            return;
        }

        for (int i = 0; i < trades.size(); i++) {
            ArrayList<Trade> remaining = new ArrayList<>();
            for (int j = i + 1; j < trades.size(); j++) remaining.add(trades.get(j));
            ArrayList<Trade> partialRec = new ArrayList<>(partial);

            partialRec.add(trades.get(i));

            recursiveFindCandidates(remaining, trxAmount, partialRec, candidates);
        }
    }

    void findCandidates(ArrayList<Trade> trades, double trxAmount, List<List<Trade>> candidates) {
        recursiveFindCandidates(trades, trxAmount, new ArrayList<>(), candidates);
    }

}
