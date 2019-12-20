package space.xrapid.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import space.xrapid.domain.*;
import space.xrapid.domain.ripple.Payment;
import space.xrapid.service.ExchangeToExchangePaymentService;
import space.xrapid.service.XrapidInboundAddressService;
import space.xrapid.util.TradesCombinaisonsHelper;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static space.xrapid.job.Scheduler.transactionHashes;
import static space.xrapid.util.TradesCombinaisonsHelper.sum;

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

    protected long buyDelta;
    protected long sellDelta;
    private static long DEFAULT_TIME_DELTA = 60;

    public XrapidCorridors(ExchangeToExchangePaymentService exchangeToExchangePaymentService, XrapidInboundAddressService xrapidInboundAddressService, SimpMessageSendingOperations messagingTemplate, List<Exchange> exchangesToExclude, Set<String> usedTradeIds) {

        this.buyDelta = 200;
        this.sellDelta = 200;

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
            return Double.valueOf(payment.getAmount()) > 140 && getDestinationExchange().equals(Exchange.byAddress(payment.getDestination())) && allExchangeAddresses.contains(payment.getSource());

        } catch (Exception e) {
            return false;
        }
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

            if (exchangeToFiatPayment.getFiatToXrpTrades() != null && !exchangeToFiatPayment.getFiatToXrpTrades().isEmpty()) {
                exchangeToFiatPayment.setSourceFiat(exchangeToFiatPayment.getFiatToXrpTrades().get(0).getExchange().getLocalFiat());
            } else if (exchangeToFiatPayment.getSourceFiat() == null) {
                exchangeToFiatPayment.setSourceFiat(exchangeToFiatPayment.getSource().getLocalFiat());
            }

            if (exchangeToFiatPayment.getXrpToFiatTrades() != null && !exchangeToFiatPayment.getXrpToFiatTrades().isEmpty()) {
                exchangeToFiatPayment.setDestinationFiat(exchangeToFiatPayment.getXrpToFiatTrades().get(0).getExchange().getLocalFiat());
            } else {
                exchangeToFiatPayment.setDestinationFiat(exchangeToFiatPayment.getDestination().getLocalFiat());
            }

            if (exchangeToFiatPayment.getDestinationFiat() != null &&
                    exchangeToFiatPayment.getDestinationFiat().equals(exchangeToFiatPayment.getSourceFiat())) {
                return;
            }

            transactionHashes.add(exchangeToFiatPayment.getTransactionHash());

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
                List<Trade> closestTrades = TradesCombinaisonsHelper.getTrades(aggregatedTrades, exchangeToExchangePayment.getAmount(), false, sellDelta);

                double sum = closestTrades.stream().mapToDouble(Trade::getAmount).sum();

                if (sum > 0 && validateAmount(exchangeToExchangePayment, sum, exchangeToExchangePayment.getDestination().isMaxTolerence())) {

                    exchangeToExchangePayment.setXrpToFiatTrades(closestTrades);
                    exchangeToExchangePayment.setXrpToFiatTradeIds(closestTrades.stream().map(Trade::getOrderId).collect(Collectors.toList()));

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
                return diff >= 2 && diff < DEFAULT_TIME_DELTA;
            }
            return diff >= 2 && diff < buyDelta;
        };
    }

    private Predicate<Trade> filterXrpToFiatTradePerDate(ExchangeToExchangePayment exchangeToExchangePayment) {
        return trade -> {
            double diff = trade.getDateTime().toEpochSecond() - exchangeToExchangePayment.getDateTime().toEpochSecond();
            if (!exchangeToExchangePayment.getDestination().isConfirmed() || !exchangeToExchangePayment.getSource().isConfirmed()) {
                return diff >= 2 && diff < DEFAULT_TIME_DELTA;
            }
            return diff >= 2 && diff < sellDelta;
        };
    }

    protected boolean fiatToXrpTradesExists(ExchangeToExchangePayment exchangeToExchangePayment) {
        if (exchangesToExclude.contains(exchangeToExchangePayment.getDestination()) && exchangesToExclude.contains(exchangeToExchangePayment.getSource())
                || exchangeToExchangePayment.getSource() == null || exchangeToExchangePayment.getDestination() == null) {
            return false;
        }

        Arrays.asList(getAggregatedOutTrades(exchangeToExchangePayment, "sell"),
                getAggregatedOutTrades(exchangeToExchangePayment, "buy")).forEach(aggregatedTrades -> {
            if (!aggregatedTrades.isEmpty() && sum(aggregatedTrades) >= exchangeToExchangePayment.getAmount()) {

                List<Trade> closestTrades = TradesCombinaisonsHelper.getTrades(aggregatedTrades, exchangeToExchangePayment.getAmount(), true, buyDelta);

                double sum = closestTrades.stream().mapToDouble(Trade::getAmount).sum();

                if (sum > 0 && validateAmount(exchangeToExchangePayment, sum, exchangeToExchangePayment.getDestination().isMaxTolerence())) {

                    exchangeToExchangePayment.setFiatToXrpTrades(closestTrades);
                    exchangeToExchangePayment.setFiatToXrpTradeIds(closestTrades.stream().map(Trade::getOrderId).collect(Collectors.toList()));
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
                .filter(payment -> !transactionHashes.contains(payment.getTransactionHash()))
                .filter(this::xrpToFiatTradesExists)
                .sorted(Comparator.comparing(ExchangeToExchangePayment::getDateTime))
                .forEach(this::persistPayment);
    }


    protected List<Trade> getAggregatedInTrades(ExchangeToExchangePayment exchangeToExchangePayment, String side) {

        return trades.stream()
                .filter(trade -> trade.getOrderId() != null)
                .filter(trade -> side.equals(trade.getSide()))
                .filter(trade -> getDestinationExchange().equals(exchangeToExchangePayment.getDestination()))
                .filter(trade -> trade.getExchange().equals(getDestinationExchange()))
                .filter(filterXrpToFiatTradePerDate(exchangeToExchangePayment))
                .filter(trade -> !tradesIdAlreadyProcessed.contains(trade.getOrderId()))
                .collect(Collectors.toList());

    }

    protected List<Trade> getAggregatedOutTrades(ExchangeToExchangePayment exchangeToExchangePayment, String side) {

        return trades.stream()
                .filter(trade -> trade.getOrderId() != null)
                .filter(trade -> side.equals(trade.getSide()))
                .filter(trade -> trade.getExchange().equals(exchangeToExchangePayment.getSource()))
                .filter(filterFiatToXrpTradePerDate(exchangeToExchangePayment))
                .filter(trade -> !tradesIdAlreadyProcessed.contains(trade.getOrderId()))
                .collect(Collectors.toList());
    }

    protected boolean validateAmount(ExchangeToExchangePayment exchangeToExchangePayment, double tradesAmountSum, boolean maxTolerence) {
        return true;
    }

    private double getTolerence(ExchangeToExchangePayment payment, boolean maxTolerence) {

        return payment.getAmount() * 0.001;
    }

    private double getTolerence(ExchangeToExchangePayment payment) {
        return getTolerence(payment, false);
    }
}
