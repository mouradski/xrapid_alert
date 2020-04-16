package space.xrapid.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.web.client.RestTemplate;
import space.xrapid.domain.Exchange;
import space.xrapid.domain.ExchangeToExchangePayment;
import space.xrapid.domain.SpottedAt;
import space.xrapid.domain.Trade;
import space.xrapid.domain.ripple.Payment;
import space.xrapid.service.ExchangeToExchangePaymentService;
import space.xrapid.service.TradesFoundCacheService;
import space.xrapid.service.XrapidInboundAddressService;
import space.xrapid.util.TradesCombinaisonsHelper;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static space.xrapid.job.Scheduler.transactionHashes;

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

    protected TradesFoundCacheService tradesFoundCacheService;

    protected RestTemplate restTemplate = new RestTemplate();

    protected long buyDelta;
    protected long sellDelta;

    private String proxyUrl;

    public XrapidCorridors(ExchangeToExchangePaymentService exchangeToExchangePaymentService, TradesFoundCacheService tradesFoundCacheService, XrapidInboundAddressService xrapidInboundAddressService, SimpMessageSendingOperations messagingTemplate, List<Exchange> exchangesToExclude, Set<String> usedTradeIds, String proxyUrl) {

        this.buyDelta = 200;
        this.sellDelta = 200;

        this.proxyUrl = proxyUrl;

        this.tradesFoundCacheService = tradesFoundCacheService;
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
                    .amount(payment.getDeliveredAmount())
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

        messagingTemplate.convertAndSend("/top/odl", payment);

        if (proxyUrl != null && !proxyUrl.isEmpty()) {

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            try {
                restTemplate.exchange(proxyUrl,
                        HttpMethod.POST, new HttpEntity(payment, headers), String.class);
            } catch (Exception e) {
                log.error("Unable to notify api with payment", e);
            }

        }
    }


    private Predicate<Trade> filterFiatToXrpTradePerDate(ExchangeToExchangePayment exchangeToExchangePayment) {
        return trade -> {
            long diff = Math.abs(ChronoUnit.SECONDS.between(exchangeToExchangePayment.getDateTime(), trade.getDateTime()));
            return exchangeToExchangePayment.getDateTime().isAfter(trade.getDateTime()) && diff < buyDelta && diff >= 2;
        };
    }

    private Predicate<Trade> filterXrpToFiatTradePerDate(ExchangeToExchangePayment exchangeToExchangePayment) {
        return trade -> {
            long diff = Math.abs(ChronoUnit.SECONDS.between(trade.getDateTime(), exchangeToExchangePayment.getDateTime()));
            return exchangeToExchangePayment.getDateTime().isBefore(trade.getDateTime()) && diff < buyDelta && diff >= 2;
        };
    }

    protected boolean xrpToFiatTradesExists(ExchangeToExchangePayment exchangeToExchangePayment) {

        if (exchangesToExclude.contains(exchangeToExchangePayment.getDestination()) && exchangesToExclude.contains(exchangeToExchangePayment.getSource())) {
            return false;
        }

        exchangeToExchangePayment.setDestinationCurrencry(exchangeToExchangePayment.getDestinationFiat());

        Arrays.asList(getAggregatedSellTrades(exchangeToExchangePayment, "sell"),
                getAggregatedSellTrades(exchangeToExchangePayment, "buy")).forEach(aggregatedTrades -> {

            if (!aggregatedTrades.isEmpty() && (exchangeToExchangePayment.getXrpToFiatTrades() == null || exchangeToExchangePayment.getXrpToFiatTrades().isEmpty())) {


                List<Trade> closestTrades = tradesFoundCacheService.getXrpToFiatTrades(exchangeToExchangePayment.getTransactionHash(), aggregatedTrades.get(0).getExchange());

                if (closestTrades == null) {
                    closestTrades = TradesCombinaisonsHelper.getTrades(aggregatedTrades, exchangeToExchangePayment.getAmount(), "sell");
                    if (!closestTrades.isEmpty()) {
                        tradesFoundCacheService.addXrpToFiatTrades(exchangeToExchangePayment.getTransactionHash(), aggregatedTrades.get(0).getExchange(), closestTrades);
                    }
                }

                double sum = closestTrades.stream().mapToDouble(Trade::getAmount).sum();

                if (sum > 0) {

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

    protected boolean fiatToXrpTradesExists(ExchangeToExchangePayment exchangeToExchangePayment) {
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

    protected void submit(List<Payment> payments) {

        if (payments.isEmpty()) {
            return;
        }

        payments.stream()
                .map(this::mapPayment)
                .filter(payment -> !transactionHashes.contains(payment.getTransactionHash()))
                .filter(this::xrpToFiatTradesExists)
                .sorted(Comparator.comparing(ExchangeToExchangePayment::getDateTime))
                .forEach(this::persistPayment);
    }


    protected List<Trade> getAggregatedSellTrades(ExchangeToExchangePayment exchangeToExchangePayment, String side) {

        return trades.stream()
                .filter(trade -> trade.getOrderId() != null)
                .filter(trade -> side.equals(trade.getSide()))
                .filter(trade -> getDestinationExchange().equals(exchangeToExchangePayment.getDestination()))
                .filter(trade -> trade.getExchange().equals(getDestinationExchange()))
                .filter(filterXrpToFiatTradePerDate(exchangeToExchangePayment))
                .filter(trade -> !tradesIdAlreadyProcessed.contains(trade.getOrderId()))
                .collect(Collectors.toList());

    }

    protected List<Trade> getAggregatedBuyTrades(ExchangeToExchangePayment exchangeToExchangePayment, String side) {

        return trades.stream()
                .filter(trade -> trade.getOrderId() != null)
                .filter(trade -> side.equals(trade.getSide()))
                .filter(trade -> trade.getExchange().equals(exchangeToExchangePayment.getSource()))
                .filter(filterFiatToXrpTradePerDate(exchangeToExchangePayment))
                .filter(trade -> !tradesIdAlreadyProcessed.contains(trade.getOrderId()))
                .collect(Collectors.toList());
    }
}
