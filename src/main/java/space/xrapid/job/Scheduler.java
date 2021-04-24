package space.xrapid.job;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import space.xrapid.domain.Exchange;
import space.xrapid.domain.Stats;
import space.xrapid.domain.Trade;
import space.xrapid.domain.ripple.Payment;
import space.xrapid.listener.EndToEndXrapidCorridors;
import space.xrapid.listener.InboundXrapidCorridors;
import space.xrapid.listener.OffchainCorridors;
import space.xrapid.listener.OutboundXrapidCorridors;
import space.xrapid.service.*;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static space.xrapid.job.Config.MAX_TRADE_DELAY_IN_MINUTES;
import static space.xrapid.job.Config.XRPL_PAYMENT_WINDOW_SIZE_IN_MINUTES;

@Slf4j
@EnableScheduling
@Component
public class Scheduler {

    public static Set<String> transactionHashes = new HashSet<>();
    public static Set<String> offChainXrpToFiatTradeIds = new HashSet<>();
    public static Set<String> offChainFiatToXrpTradeIds = new HashSet<>();
    @Autowired
    protected SimpMessageSendingOperations messagingTemplate;
    @Autowired
    private List<TradeService> tradeServices;
    @Autowired
    private XrpLedgerService xrpLedgerService;
    @Autowired
    private ExchangeToExchangePaymentService exchangeToExchangePaymentService;
    @Autowired
    private XrapidInboundAddressService xrapidInboundAddressService;
    @Autowired
    private TwitterService twitterService;
    @Autowired
    private RateService rateService;
    @Autowired
    private TradesFoundCacheService tradesFoundCacheService;
    @Autowired
    private DestinationTagRepeatService destinationTagRepeatService;
    @Value("${api.proxy:false}")
    private boolean proxy;
    @Value("${api.proxy.url:}")
    private String proxyUrl;
    private ExecutorService executorService = Executors.newFixedThreadPool(7);

    private OffsetDateTime lastWindowEnd;
    private OffsetDateTime windowStart;
    private OffsetDateTime windowEnd;

    private Set<String> proceededTrades = new HashSet<>();

    @Scheduled(fixedDelay = 40000)
    public void offchainOdl() {

        if (proxy) {
            return;
        }

        OffsetDateTime start = OffsetDateTime.now(ZoneOffset.UTC);

        double rate = rateService.getXrpUsdRate();

        Stream.of(Exchange.values())
                .filter(Exchange::isConfirmed)
                .collect(Collectors.groupingBy(Exchange::getName))
                .values().stream().filter(exchanges -> exchanges.size() > 1)
                .forEach(exchanges -> {

                    List<Trade> trades = new ArrayList<>();

                    tradeServices.stream().
                            filter(service -> exchanges.contains(service.getExchange()))
                            .forEach(tradeService -> {
                                try {
                                    trades.addAll(tradeService.fetchTrades(start.minusSeconds(90)));
                                } catch (Exception e) {
                                    log.error("Unable to fetch {} trades", tradeService.getExchange(), e);
                                }
                            });

                    exchanges.forEach(sourceMarket -> {
                        exchanges.stream()
                                .filter(destinationMarket -> !destinationMarket.equals(sourceMarket))
                                .forEach(destinationMarket -> {
                                    log.info("Searching Offchain ODL TRX within '{}' for {}->{} corridor", sourceMarket.getName(), sourceMarket.getLocalFiat(), destinationMarket.getLocalFiat());
                                    new OffchainCorridors(exchangeToExchangePaymentService, messagingTemplate, sourceMarket,
                                            destinationMarket, offChainFiatToXrpTradeIds, offChainXrpToFiatTradeIds)
                                            .searchXrapidPayments(trades, rate);
                                });
                    });

                });

    }

    @Scheduled(fixedRate = 56000)
    public void odl() {

        if (proxy) {
            return;
        }

        List<Exchange> allConfirmedExchange = Stream.of(Exchange.values()).collect(Collectors.toList());
        List<Exchange> availableExchangesWithApi = tradeServices.stream().map(TradeService::getExchange)
                .collect(Collectors.toList());

        updatePaymentsWindows();

        OffsetDateTime xrplPaymentsStart = windowEnd
                .minusMinutes(MAX_TRADE_DELAY_IN_MINUTES + XRPL_PAYMENT_WINDOW_SIZE_IN_MINUTES);
        OffsetDateTime xrplPaymentsEnd = windowEnd.minusMinutes(MAX_TRADE_DELAY_IN_MINUTES);
        log.info("Fetching ODL candidates from XRP Ledger, from {} to {}", xrplPaymentsStart,
                xrplPaymentsEnd);
        List<Payment> payments = xrpLedgerService
                .fetchOdlCandidatePayments(xrplPaymentsStart, xrplPaymentsEnd, true);

        log.info("{} ODL candidates fetched from XRP Ledger", payments.size());

        if (payments.isEmpty()) {
            return;
        }

        List<Trade> allTrades = fetchTrades();

        double rate = rateService.getXrpUsdRate();

        log.info(
                "Searching all ODL TRX between exchanges that providing API for new corridors basing on trades sum matching on both exchanges");
        endToEndSearch(availableExchangesWithApi, payments, allTrades, rate);

        log.info(
                "Searching all ODL TRX between all exchanges, that are followed by a sell in the local currency (in case source exchange not providing API)");
        atDestinationSearch(availableExchangesWithApi, payments, allTrades, rate);

        log.info(
                "Searching for all ODL TRX from exchanges with API to all exchanes (in case destination exchange not providing API)");
        atSourceSearch(allConfirmedExchange, availableExchangesWithApi, payments, allTrades, rate);

        log.info(
                "Searching all ODL TRX between exchanges that providing API, basing on confirmed destination tag");
        byDestinationTagSearch(availableExchangesWithApi, payments, allTrades, rate);

        Stats stats = exchangeToExchangePaymentService.calculateStats(21);

        if (stats != null) {
            messagingTemplate
                    .convertAndSend("/topic/stats", exchangeToExchangePaymentService.calculateStats(21));
        }
    }

    private List<Trade> fetchTrades() {
        List<Trade> allTrades = new ArrayList<>();

        tradeServices.parallelStream()
                .filter(service -> service.getExchange().isConfirmed())
                .forEach(tradeService -> {
                    try {
                        OffsetDateTime sellTradesStart = windowEnd.minusMinutes(
                                MAX_TRADE_DELAY_IN_MINUTES + XRPL_PAYMENT_WINDOW_SIZE_IN_MINUTES
                                        + MAX_TRADE_DELAY_IN_MINUTES);
                        List<Trade> trades = tradeService.fetchTrades(sellTradesStart);
                        allTrades.addAll(trades);
                        log.info("{} trades fetched from {} from {}", trades.size(), tradeService.getExchange(),
                                sellTradesStart);
                    } catch (Exception e) {
                        log.error("Error fetching {} trades", tradeService.getExchange());
                    }
                });
        return allTrades;
    }

    private void byDestinationTagSearch(List<Exchange> availableExchangesWithApi,
                                        List<Payment> payments, List<Trade> allTrades, double rate) {
        availableExchangesWithApi.forEach(sourceExchange -> {
            availableExchangesWithApi.stream()
                    .filter(destinationExchange -> !destinationExchange.equals(sourceExchange))
                    .forEach(destinationExchange -> {
                        executorService.execute(() -> {
                            new EndToEndXrapidCorridors(exchangeToExchangePaymentService, tradesFoundCacheService,
                                    xrapidInboundAddressService, messagingTemplate, sourceExchange,
                                    destinationExchange, 90, 90, false, null, proxyUrl)
                                    .searchXrapidPayments(payments, allTrades, rate);
                        });
                    });
        });

    }

    private void atSourceSearch(List<Exchange> allConfirmedExchange,
                                List<Exchange> availableExchangesWithApi, List<Payment> payments, List<Trade> allTrades,
                                double rate) {
        allConfirmedExchange.stream()
                .filter(exchange -> !availableExchangesWithApi.contains(exchange))
                .forEach(exchange -> {
                    executorService.execute(() -> {
                        new OutboundXrapidCorridors(exchangeToExchangePaymentService, tradesFoundCacheService,
                                messagingTemplate, exchange, availableExchangesWithApi, proxyUrl, null)
                                .searchXrapidPayments(payments, allTrades, rate);
                    });
                });
    }

    private void atDestinationSearch(List<Exchange> availableExchangesWithApi, List<Payment> payments,
                                     List<Trade> allTrades, double rate) {
        availableExchangesWithApi.forEach(exchange -> {
            executorService.execute(() -> {
                new InboundXrapidCorridors(exchangeToExchangePaymentService, tradesFoundCacheService,
                        messagingTemplate, exchange, availableExchangesWithApi, proxyUrl, null)
                        .searchXrapidPayments(payments,
                                allTrades.stream().filter(trade -> trade.getExchange().equals(exchange))
                                        .collect(Collectors.toList()), rate);
            });
        });
    }

    private void endToEndSearch(List<Exchange> availableExchangesWithApi, List<Payment> payments,
                                List<Trade> allTrades, double rate) {
        availableExchangesWithApi.forEach(sourceExchange -> {
            availableExchangesWithApi.stream()
                    .filter(destinationExchange -> !destinationExchange.equals(sourceExchange))
                    .forEach(destinationExchange -> {
                        executorService.execute(() -> {
                            new EndToEndXrapidCorridors(exchangeToExchangePaymentService, tradesFoundCacheService,
                                    xrapidInboundAddressService, messagingTemplate, sourceExchange,
                                    destinationExchange, MAX_TRADE_DELAY_IN_MINUTES * 60,
                                    MAX_TRADE_DELAY_IN_MINUTES * 60, true, proceededTrades, proxyUrl)
                                    .searchXrapidPayments(payments, allTrades, rate);
                        });
                    });
        });
    }

    private void updatePaymentsWindows() {
        windowEnd = OffsetDateTime.now(ZoneOffset.UTC);
        windowStart = windowEnd.minusMinutes(20);

        if (lastWindowEnd != null) {
            windowStart = lastWindowEnd;
        }

        lastWindowEnd = windowEnd;
    }

    @Scheduled(cron = "0 15 2 1/1 * ?")
    public void dailyTweetBot() {
        twitterService.dailySummary(exchangeToExchangePaymentService.calculateGlobalStats(false));
    }

    @Scheduled(cron = "0 0 0 * * *")
    public void tags() {
        OffsetDateTime end = OffsetDateTime.now(ZoneOffset.UTC);
        OffsetDateTime start = end.minusHours(24);

        Map<String, List<Payment>> map = xrpLedgerService.fetchOdlCandidatePayments(start, end, false)
                .stream()
                .filter(p -> p.getDestinationTag() != null && p.getDestinationTag() != 0)
                .collect(Collectors.groupingBy(
                        p -> new StringBuilder().append(p.getSource()).append(":").append(p.getDestination())
                                .append(":").append(p.getDestinationTag()).toString()));

        destinationTagRepeatService.purge();

        for (Map.Entry<String, List<Payment>> e : map.entrySet()) {
            String[] key = e.getKey().split(":");

            String sourceAddress = key[0];
            String destinationAddress = key[1];
            Long destinationTag = Long.valueOf(key[2]);

            String source = null;
            String destiantion = null;

            if (Exchange.byAddress(sourceAddress) != null) {
                source = Exchange.byAddress(sourceAddress).getName();
            }

            if (Exchange.byAddress(destinationAddress) != null) {
                destiantion = Exchange.byAddress(destinationAddress).getName();
            }

            Long todayRepeat = Long.valueOf(e.getValue().size());

            Double sum = e.getValue().stream().mapToDouble(Payment::getAmount).sum();

            if (todayRepeat > 10) {
                destinationTagRepeatService
                        .add(sourceAddress, destinationAddress, source, destiantion, todayRepeat,
                                destinationTag, sum);
            }
        }
    }

    @Scheduled(fixedDelay = 10000000)
    private void purgeProceededTrades() {
        proceededTrades.clear();
    }

}
