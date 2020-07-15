package space.xrapid.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import space.xrapid.domain.*;
import space.xrapid.domain.Currency;
import space.xrapid.domain.ripple.Payment;
import space.xrapid.service.ExchangeToExchangePaymentService;
import space.xrapid.service.TradesFoundCacheService;
import space.xrapid.service.XrapidInboundAddressService;
import space.xrapid.util.TradesCombinaisonsHelper;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import static space.xrapid.job.Scheduler.transactionHashes;

@Slf4j
public class EndToEndXrapidCorridors extends XrapidCorridors {

    private Exchange destinationExchange;

    private Currency destinationFiat;

    private Currency sourceFiat;

    private boolean requireEndToEnd;

    public Exchange getDestinationExchange() {
        return destinationExchange;
    }

    public Currency getDestinationFiat() {
        return destinationFiat;
    }

    public Currency getSourceFiat() {return sourceFiat;}


    public EndToEndXrapidCorridors(ExchangeToExchangePaymentService exchangeToExchangePaymentService, TradesFoundCacheService tradesFoundCacheService, XrapidInboundAddressService xrapidInboundAddressService,
                                   SimpMessageSendingOperations messagingTemplate, Exchange exchange, Currency destinationFiat, long buyDelta, long sellDelta, boolean requireEndToEnd, Set<String> tradeIds, String proxyUrl) {

        super(exchangeToExchangePaymentService, tradesFoundCacheService, xrapidInboundAddressService, messagingTemplate, null, tradeIds, proxyUrl);


        this.buyDelta = buyDelta;
        this.sellDelta = sellDelta;

        this.requireEndToEnd = requireEndToEnd;

        this.destinationFiat = destinationFiat;
        if (exchange != null) {
            this.sourceFiat = exchange.getLocalFiat();

        }
        this.destinationExchange = exchange;
    }

    public void searchXrapidPayments(List<Payment> payments, List<Trade> trades, double rate) {
        this.rate = rate;

        tradesIdAlreadyProcessed = new HashSet<>();

        this.trades = trades;
        submit(payments);
    }

    @Override
    public SpottedAt getSpottedAt() {
        return SpottedAt.SOURCE_AND_DESTINATION;
    }

    @Override
    protected void submit(List<Payment> payments) {

        if (payments.isEmpty()) {
            return;
        }

        if (requireEndToEnd) {
            payments.stream()
                    .map(this::mapPayment)
                    .filter(payment -> !transactionHashes.contains(payment.getTransactionHash()))
                    .filter(payment -> fiatToXrpTradesExists(payment) && xrpToFiatTradesExists(payment))
                    .sorted(Comparator.comparing(ExchangeToExchangePayment::getTimestamp))
                    .forEach(this::persistPayment);

        } else {
            payments.stream()
                    .map(this::mapPayment)
                    .filter(payment -> this.getDestinationExchange().equals(payment.getDestination()))
                    .peek(payment -> payment.setSourceFiat(this.destinationFiat))
                    .filter(xrapidInboundAddressService::isXrapidDestination)
                    .peek(payment -> payment.setSpottedAt(SpottedAt.DESTINATION_TAG))
                    .sorted(Comparator.comparing(ExchangeToExchangePayment::getTimestamp))
                    .forEach(payment -> persistPayment(payment));
        }
    }

    @Override
    protected boolean fiatToXrpTradesExists(ExchangeToExchangePayment exchangeToExchangePayment) {

        exchangeToExchangePayment.setSource(Exchange.byAddress(exchangeToExchangePayment.getSourceAddress(), this.getSourceFiat()));

        if (exchangesToExclude.contains(exchangeToExchangePayment.getDestination()) && exchangesToExclude.contains(exchangeToExchangePayment.getSource())
            || exchangeToExchangePayment.getSource() == null || exchangeToExchangePayment.getDestination() == null) {
            return false;
        }

        Arrays.asList(getAggregatedBuyTrades(exchangeToExchangePayment, "buy"), getAggregatedBuyTrades(exchangeToExchangePayment, "sell"))
            .forEach(aggregatedTrades -> {
                if (!aggregatedTrades.isEmpty() && (exchangeToExchangePayment.getFiatToXrpTrades() == null || exchangeToExchangePayment.getFiatToXrpTrades().isEmpty())) {

                    List<Trade> closestTrades = tradesFoundCacheService.getFiatToXrpTrades(exchangeToExchangePayment.getTransactionHash(), aggregatedTrades.get(0).getExchange());

                    if (closestTrades == null) {
                        closestTrades = TradesCombinaisonsHelper.getTrades(aggregatedTrades, exchangeToExchangePayment.getAmount(), "buy");

                        if (!closestTrades.isEmpty()) {
                            tradesFoundCacheService.addFiatToXrpTrades(exchangeToExchangePayment.getTransactionHash(), aggregatedTrades.get(0).getExchange(), closestTrades);
                        }
                    }

                    double sum = closestTrades.stream().mapToDouble(Trade::getAmount).sum();

                    if (sum > 0) {

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

    @Override
    protected ExchangeToExchangePayment mapPayment(Payment payment) {
        try {
            Exchange source = Exchange.byAddress(payment.getSource());
            Exchange destination = Exchange.byAddress(payment.getDestination());
            boolean xrapidCorridorConfirmed = source.isConfirmed() && destination.isConfirmed();

            OffsetDateTime dateTime = OffsetDateTime.parse(payment.getExecutedTime(), DateTimeFormatter.ISO_OFFSET_DATE_TIME);

            return ExchangeToExchangePayment.builder()
                    .amount(payment.getDeliveredAmount())
                    .destination(Exchange.byAddress(payment.getDestination()))
                    .source(Exchange.byAddress(payment.getSource(), getDestinationFiat()))
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


    @Override
    protected List<Trade> getAggregatedSellTrades(ExchangeToExchangePayment exchangeToExchangePayment, String side) {

        return trades.stream()
            .filter(trade -> trade.getOrderId() != null)
            .filter(trade -> side.equals(trade.getSide()))
            .filter(trade -> trade.getExchange().equals(exchangeToExchangePayment.getDestination()))
            .filter(filterXrpToFiatTradePerDate(exchangeToExchangePayment))
            .filter(trade -> !tradesIdAlreadyProcessed.contains(trade.getOrderId()))
            .collect(Collectors.toList());

    }
}
