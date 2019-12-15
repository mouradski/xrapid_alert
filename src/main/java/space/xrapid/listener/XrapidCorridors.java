package space.xrapid.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import space.xrapid.domain.*;
import space.xrapid.domain.ripple.Payment;
import space.xrapid.service.ExchangeToExchangePaymentService;
import space.xrapid.service.XrapidInboundAddressService;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Slf4j
public abstract class XrapidCorridors {

    protected List<Trade> trades = new ArrayList<>();

    protected Set<String> allExchangeAddresses;
    protected Set<String> tradesIdAlreadyProcessed;

    protected double rate;

    protected ExchangeToExchangePaymentService exchangeToExchangePaymentService;

    protected XrapidInboundAddressService xrapidInboundAddressService;

    protected List<Exchange> exchangesToExclude;

    protected SimpMessageSendingOperations messagingTemplate;

    ExecutorService executorService = new ThreadPoolExecutor(1, 3, 1000,
            TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());

    protected long buyDelta;
    protected long sellDelta;
    private static long DEFAULT_TIME_DELTA = 60;

    public XrapidCorridors(ExchangeToExchangePaymentService exchangeToExchangePaymentService, XrapidInboundAddressService xrapidInboundAddressService, SimpMessageSendingOperations messagingTemplate, List<Exchange> exchangesToExclude, Set<String> usedTradeIds) {

        this.buyDelta = 140;
        this.sellDelta = 140;

        if (exchangesToExclude == null) {
            this.exchangesToExclude = new ArrayList<>();
        } else {
            this.exchangesToExclude = exchangesToExclude;
        }
        this.exchangeToExchangePaymentService = exchangeToExchangePaymentService;
        this.xrapidInboundAddressService = xrapidInboundAddressService;
        this.messagingTemplate = messagingTemplate;

        if (usedTradeIds == null) {
            this.tradesIdAlreadyProcessed = new HashSet<>();
        } else {
            this.tradesIdAlreadyProcessed = usedTradeIds;
        }

        allExchangeAddresses = Arrays.stream(Exchange.values()).map(e -> e.getAddresses()).flatMap(Arrays::stream)
                .collect(Collectors.toSet());
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
            return Double.valueOf(payment.getAmount()) > 40 && getDestinationExchange().equals(Exchange.byAddress(payment.getDestination())) && allExchangeAddresses.contains(payment.getSource());

        } catch (Exception e) {
            return false;
        }
    }

    protected List<TradesGroup> takeClosesTrades(ExchangeToExchangePayment exchangeToExchangePayment, List<List<TradesGroup>> groupedXrpTrades) {

        return groupedXrpTrades.stream()
                .sorted(Comparator.comparing(tradesGroup -> getAmountDelta(exchangeToExchangePayment, tradesGroup)))
                .findFirst().orElse(new ArrayList<>());
    }

    protected double getAmountDelta(ExchangeToExchangePayment exchangeToExchangePayment, List<TradesGroup> tradesGroup) {
        return Double.valueOf(Math.abs(exchangeToExchangePayment.getAmount() - totalAmount(tradesGroup)));
    }

    protected double totalAmount(List<TradesGroup> trades) {
        return trades.stream().mapToDouble(TradesGroup::getSum).sum();
    }

    protected void persistPayment(ExchangeToExchangePayment exchangeToFiatPayment) {
        try {
            exchangeToFiatPayment.setUsdValue(exchangeToFiatPayment.getAmount() * rate);

            if (exchangeToFiatPayment.getFiatToXrpTrades() != null) {
                exchangeToFiatPayment.setSourceFiat(exchangeToFiatPayment.getFiatToXrpTrades().get(0).getExchange().getLocalFiat());
            } else if (exchangeToFiatPayment.getSourceFiat() == null) {
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
                if (SpottedAt.SOURCE_AND_DESTINATION.equals(exchangeToFiatPayment.getSpottedAt()) && xrapidInboundAddressService != null) {
                    xrapidInboundAddressService.add(exchangeToFiatPayment);
                    log.info("{}:{} added as ODL destination candidate.", exchangeToFiatPayment.getDestinationAddress(), exchangeToFiatPayment.getTag());
                }
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


        Arrays.asList(getAggregatedInTrades(exchangeToExchangePayment, "sell"),
                getAggregatedInTrades(exchangeToExchangePayment, "buy")).forEach(aggregatedTrades -> {

            if (!aggregatedTrades.isEmpty()) {

                List<List<TradesGroup>> result = new ArrayList<>();

                List<TradesGroup> tradesGroups = new ArrayList<>();

                for (Map.Entry<String, List<Trade>> e : aggregatedTrades.entrySet()) {
                    tradesGroups.add(TradesGroup.builder().sum(e.getValue().stream().mapToDouble(Trade::getAmount).sum()).id(e.getKey()).build());
                }

                findCandidates(tradesGroups, exchangeToExchangePayment, result, exchangeToExchangePayment.getDestination().isMaxTolerence());

                List<TradesGroup> closestGroups = takeClosesTrades(exchangeToExchangePayment, result);

                double sum = closestGroups.stream().mapToDouble(TradesGroup::getSum).sum();

                if (validateAmount(exchangeToExchangePayment, sum, exchangeToExchangePayment.getDestination().isMaxTolerence())) {

                    List<Trade> closestTrades = closestGroups.stream().map(tradesGroup -> aggregatedTrades.get(tradesGroup.getId())).flatMap(List::stream).collect(Collectors.toList());

                    exchangeToExchangePayment.setXrpToFiatTrades(closestTrades);

                    String tradeIds = closestTrades.stream().map(Trade::getOrderId).collect(Collectors.joining(";"));
                    exchangeToExchangePayment.setInTradeFound(true);
                    exchangeToExchangePayment.setTradeIds(tradeIds);

                    tradesIdAlreadyProcessed.addAll(closestTrades.stream().map(Trade::getOrderId).collect(Collectors.toList()));
                }
            }
        });

        if (!exchangeToExchangePayment.isInTradeFound() && SpottedAt.SOURCE_AND_DESTINATION.equals(getSpottedAt())) {
            exchangeToExchangePayment.getFiatToXrpTrades().stream()
                    .map(Trade::getOrderId).forEach(orderId -> {
                tradesIdAlreadyProcessed.remove(orderId);
            });
        }

        return exchangeToExchangePayment.isInTradeFound();
    }

    private Predicate<Trade> filterFiatToXrpTradePerDate(ExchangeToExchangePayment exchangeToExchangePayment) {
        return trade -> {
            double diff = exchangeToExchangePayment.getDateTime().toEpochSecond() - trade.getDateTime().toEpochSecond();
            if (!exchangeToExchangePayment.getDestination().isConfirmed() || !exchangeToExchangePayment.getSource().isConfirmed()) {
                return diff >= 1 && diff < DEFAULT_TIME_DELTA;
            }
            return diff >= 1 && diff < buyDelta;
        };
    }

    private Predicate<Trade> filterXrpToFiatTradePerDate(ExchangeToExchangePayment exchangeToExchangePayment) {
        return trade -> {
            double diff = trade.getDateTime().toEpochSecond() - exchangeToExchangePayment.getDateTime().toEpochSecond();
            if (!exchangeToExchangePayment.getDestination().isConfirmed() || !exchangeToExchangePayment.getSource().isConfirmed()) {
                return diff >= 1 && diff < DEFAULT_TIME_DELTA;
            }
            return diff >= 1 && diff < sellDelta;
        };
    }

    protected boolean fiatToXrpTradesExists(ExchangeToExchangePayment exchangeToExchangePayment) {

        if (exchangesToExclude.contains(exchangeToExchangePayment.getDestination()) && exchangesToExclude.contains(exchangeToExchangePayment.getSource())
                || exchangeToExchangePayment.getSource() == null || exchangeToExchangePayment.getDestination() == null) {
            return false;
        }

        Arrays.asList(getAggregatedOutTrades(exchangeToExchangePayment, "sell"),
                getAggregatedOutTrades(exchangeToExchangePayment, "buy")).forEach(aggregatedTrades -> {
            if (!aggregatedTrades.isEmpty()) {
                List<List<TradesGroup>> result = new ArrayList<>();

                List<TradesGroup> tradesGroups = new ArrayList<>();

                for (Map.Entry<String, List<Trade>> e : aggregatedTrades.entrySet()) {
                    tradesGroups.add(TradesGroup.builder().sum(e.getValue().stream().mapToDouble(Trade::getAmount).sum()).id(e.getKey()).build());
                }

                findCandidates(tradesGroups, exchangeToExchangePayment, result, exchangeToExchangePayment.getDestination().isMaxTolerence());

                List<TradesGroup> closestGroups = takeClosesTrades(exchangeToExchangePayment, result);

                double sum = closestGroups.stream().mapToDouble(TradesGroup::getSum).sum();

                if (validateAmount(exchangeToExchangePayment, sum, exchangeToExchangePayment.getDestination().isMaxTolerence())) {

                    List<Trade> closestTrades = closestGroups.stream().map(tradesGroup -> aggregatedTrades.get(tradesGroup.getId())).flatMap(List::stream).collect(Collectors.toList());

                    exchangeToExchangePayment.setFiatToXrpTrades(closestTrades);

                    String tradeIds = closestTrades.stream().map(Trade::getOrderId).collect(Collectors.joining(";"));
                    exchangeToExchangePayment.setOutTradeFound(true);
                    exchangeToExchangePayment.setTradeOutIds(tradeIds);

                    tradesIdAlreadyProcessed.addAll(closestTrades.stream().map(Trade::getOrderId).collect(Collectors.toList()));
                }
            }
        });

        return exchangeToExchangePayment.isOutTradeFound();
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


    protected Map<String, List<Trade>> getAggregatedInTrades(ExchangeToExchangePayment exchangeToExchangePayment, String side) {

        return trades.stream()
                .filter(trade -> trade.getOrderId() != null)
                .filter(trade -> side.equals(trade.getSide()))
                .filter(trade -> getDestinationExchange().equals(exchangeToExchangePayment.getDestination()))
                .filter(trade -> trade.getExchange().equals(getDestinationExchange()))
                .filter(filterXrpToFiatTradePerDate(exchangeToExchangePayment))
                .filter(trade -> !tradesIdAlreadyProcessed.contains(trade.getOrderId()))
                .collect(Collectors.groupingBy(Trade::getDateTimeAndOrderSide));
    }

    protected Map<String, List<Trade>> getAggregatedOutTrades(ExchangeToExchangePayment exchangeToExchangePayment, String side) {

        return trades.stream()
                .filter(trade -> trade.getOrderId() != null)
                .filter(trade -> side.equals(trade.getSide()))
                .filter(trade -> trade.getExchange().equals(exchangeToExchangePayment.getSource()))
                .filter(filterFiatToXrpTradePerDate(exchangeToExchangePayment))
                .filter(trade -> !tradesIdAlreadyProcessed.contains(trade.getOrderId()))
                .collect(Collectors.groupingBy(Trade::getDateTimeAndOrderSide));
    }

    protected boolean validateAmount(ExchangeToExchangePayment exchangeToExchangePayment, double tradesAmountSum, boolean maxTolerence) {
        return (Math.abs(exchangeToExchangePayment.getAmount() - tradesAmountSum) < getTolerence(exchangeToExchangePayment, maxTolerence));
    }

    protected double sum(List<Trade> trades) {
        return trades.stream().mapToDouble(Trade::getAmount).sum();
    }

    private double getTolerence(ExchangeToExchangePayment payment, boolean maxTolerence) {
        double tolerence = 0.5;

        if (payment.getAmount() > 10000) {
            tolerence = payment.getAmount() * 0.01;
        }

        if (maxTolerence) {
            if (payment.getAmount() > 1000) {
                tolerence = payment.getAmount() * 0.01;
            }

            if (payment.getAmount() > 40000) {
                tolerence = payment.getAmount() * 0.25;
            }
        }

        return tolerence;
    }

    private double getTolerence(ExchangeToExchangePayment payment) {
        return getTolerence(payment, false);
    }

    void recursiveFindCandidates(List<TradesGroup> trades, ExchangeToExchangePayment payment, List<TradesGroup> partial, List<List<TradesGroup>> candidates, boolean maxTolerence) {

        double sum = partial.stream().mapToDouble(TradesGroup::getSum).sum();

        if (Math.abs(sum - payment.getAmount()) < getTolerence(payment, maxTolerence)) {
            candidates.add(partial);
        }

        if (sum >= payment.getAmount() + getTolerence(payment, maxTolerence)) {
            return;
        }

        for (int i = 0; i < trades.size(); i++) {
            ArrayList<TradesGroup> remaining = new ArrayList<>();
            for (int j = i + 1; j < trades.size(); j++) remaining.add(trades.get(j));
            ArrayList<TradesGroup> partialRec = new ArrayList<>(partial);

            partialRec.add(trades.get(i));
            recursiveFindCandidates(remaining, payment, partialRec, candidates, maxTolerence);
        }
    }

    void findCandidates(List<TradesGroup> trades, ExchangeToExchangePayment payment, List<List<TradesGroup>> candidates, boolean maxTolerence) {
        recursiveFindCandidates(trades, payment, new ArrayList<>(), candidates, maxTolerence);
    }

}
