package space.xrapid.job;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import space.xrapid.domain.Currency;
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

@Slf4j
@EnableScheduling
@Component
public class Scheduler {

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
    protected SimpMessageSendingOperations messagingTemplate;

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

    public static Set<String> transactionHashes = new HashSet<>();
    public static Set<String> offChainXrpToFiatTradeIds = new HashSet<>();
    public static Set<String> offChainFiatToXrpTradeIds = new HashSet<>();


    private static int MAX_TRADE_DELAY_IN_MINUTES = 6;
    private static int XRPL_PAYMENT_WINDOW_SIZE_IN_MINUTES = 1;

    private ExecutorService executorService = Executors.newFixedThreadPool(7);

    private OffsetDateTime lastWindowEnd;
    private OffsetDateTime windowStart;
    private OffsetDateTime windowEnd;


    @Scheduled(fixedDelay = 40000)
    public void offchainOdl() {
        OffsetDateTime start = OffsetDateTime.now(ZoneOffset.UTC);

        List<Trade> trades = new ArrayList<>();

        List<Exchange> bitstampMarkets = Arrays.asList(Exchange.BITSTAMP, Exchange.BITSTAMP_EUR, Exchange.BITSTAMP_GBP);

        tradeServices.stream().
                filter(service -> bitstampMarkets.contains(service.getExchange()))
                .forEach(tradeService -> {
                    try {
                        trades.addAll(tradeService.fetchTrades(start.minusSeconds(90)));
                    } catch (Exception e) {
                        log.error("Unable to fetch {} trades", tradeService.getExchange(), e);
                    }
                });

        double rate = rateService.getXrpUsdRate();

        bitstampMarkets.forEach(sourceMarket -> {
            bitstampMarkets.stream()
                    .filter(destinationMarket -> !destinationMarket.equals(sourceMarket))
                    .forEach(destinationMarket -> {
                        new OffchainCorridors(exchangeToExchangePaymentService, messagingTemplate, sourceMarket, destinationMarket, offChainFiatToXrpTradeIds, offChainXrpToFiatTradeIds).searchXrapidPayments(trades, rate);
                    });
        });
    }

    @Scheduled(fixedRate = 56000)
    public void odl() {

        if (proxy) {
            return;
        }

        List<Exchange> allConfirmedExchange = Stream.of(Exchange.values()).collect(Collectors.toList());
        List<Exchange> availableExchangesWithApi = tradeServices.stream().map(TradeService::getExchange).collect(Collectors.toList());

        Set<Currency> destinationFiats = availableExchangesWithApi.stream().map(Exchange::getLocalFiat).collect(Collectors.toSet());

        updatePaymentsWindows();

        OffsetDateTime xrplPaymentsStart = windowEnd.minusMinutes(MAX_TRADE_DELAY_IN_MINUTES + XRPL_PAYMENT_WINDOW_SIZE_IN_MINUTES);
        OffsetDateTime xrplPaymentsEnd = windowEnd.minusMinutes(MAX_TRADE_DELAY_IN_MINUTES);
        log.info("Fetching ODL candidates from XRP Ledger, from {} to {}", xrplPaymentsStart, xrplPaymentsEnd);
        List<Payment> payments = xrpLedgerService.fetchOdlCandidatePayments(xrplPaymentsStart, xrplPaymentsEnd, true);

        log.info("{} ODL candidates fetched from XRP Ledger", payments.size());

        if (payments.isEmpty()) {
            return;
        }

        List<Trade> allTrades = fetchTrades();

        double rate = rateService.getXrpUsdRate();

        log.info("Search all ODL TRX between exchanges that providing API for new corridors basing on trades sum matching on both exchanges");
        endToEndSearch(availableExchangesWithApi, destinationFiats, payments, allTrades, rate);


        log.info("Search all ODL TRX between all exchanges, that are followed by a sell in the local currency (in case source exchange not providing API)");
        atDestinationSearch(availableExchangesWithApi, payments, allTrades, rate);

        log.info("Search for all ODL TRX from exchanges with API to all exchanes (in case destination exchange not providing API)");
        atSourceSearch(allConfirmedExchange, availableExchangesWithApi, payments, allTrades, rate);


        log.info("Search all ODL TRX between exchanges that providing API, basing on confirmed destination tag");
        byDestinationTagSearch(availableExchangesWithApi, destinationFiats, payments, allTrades, rate);

        Stats stats = exchangeToExchangePaymentService.calculateStats(21);

        if (stats != null) {
            messagingTemplate.convertAndSend("/topic/stats", exchangeToExchangePaymentService.calculateStats(21));
        }


    }

    private List<Trade> fetchTrades() {
        List<Trade> allTrades = new ArrayList<>();

        tradeServices.parallelStream()
                .filter(service -> service.getExchange().isConfirmed())
                .forEach(tradeService -> {
                    try {
                        OffsetDateTime sellTradesStart = windowEnd.minusMinutes(MAX_TRADE_DELAY_IN_MINUTES + XRPL_PAYMENT_WINDOW_SIZE_IN_MINUTES + MAX_TRADE_DELAY_IN_MINUTES);
                        List<Trade> trades = tradeService.fetchTrades(sellTradesStart);
                        allTrades.addAll(trades);
                        log.info("{} trades fetched from {} from {}", trades.size(), tradeService.getExchange(), sellTradesStart);
                    } catch (Exception e) {
                        log.error("Error fetching {} trades", tradeService.getExchange());
                    }
                });
        return allTrades;
    }

    private void byDestinationTagSearch(List<Exchange> availableExchangesWithApi, Set<Currency> destinationFiats, List<Payment> payments, List<Trade> allTrades, double rate) {
        destinationFiats.forEach(fiat -> {
            availableExchangesWithApi.stream()
                    .filter(exchange -> !exchange.getLocalFiat().equals(fiat))
                    .forEach(exchange -> {
                        executorService.execute(() -> {
                            new EndToEndXrapidCorridors(exchangeToExchangePaymentService, tradesFoundCacheService, xrapidInboundAddressService, messagingTemplate, exchange, fiat, 90, 90, false, null, proxyUrl)
                                    .searchXrapidPayments(payments, allTrades, rate);
                        });
                    });
        });
    }

    private void atSourceSearch(List<Exchange> allConfirmedExchange, List<Exchange> availableExchangesWithApi, List<Payment> payments, List<Trade> allTrades, double rate) {
        allConfirmedExchange.stream()
                .filter(exchange -> !availableExchangesWithApi.contains(exchange))
                .forEach(exchange -> {
                    executorService.execute(() -> {
                        new OutboundXrapidCorridors(exchangeToExchangePaymentService, tradesFoundCacheService, messagingTemplate, exchange, availableExchangesWithApi, proxyUrl).searchXrapidPayments(payments, allTrades, rate);
                    });
                });
    }

    private void atDestinationSearch(List<Exchange> availableExchangesWithApi, List<Payment> payments, List<Trade> allTrades, double rate) {
        availableExchangesWithApi.forEach(exchange -> {
            executorService.execute(() -> {
                new InboundXrapidCorridors(exchangeToExchangePaymentService, tradesFoundCacheService, messagingTemplate, exchange, availableExchangesWithApi, proxyUrl).searchXrapidPayments(payments, allTrades.stream().filter(trade -> trade.getExchange().equals(exchange)).collect(Collectors.toList()), rate);
            });
        });
    }

    private void endToEndSearch(List<Exchange> availableExchangesWithApi, Set<Currency> destinationFiats, List<Payment> payments, List<Trade> allTrades, double rate) {
        destinationFiats.forEach(destinationFiat -> {
            availableExchangesWithApi.stream()
                    .filter(exchange -> !exchange.getLocalFiat().equals(destinationFiat))
                    .forEach(exchange -> {
                        final Set<String> tradeIds = new HashSet<>();
                        Arrays.asList(60 * MAX_TRADE_DELAY_IN_MINUTES).forEach(delta -> {
                                executorService.execute(() -> {
                                    new EndToEndXrapidCorridors(exchangeToExchangePaymentService, tradesFoundCacheService, xrapidInboundAddressService, messagingTemplate, exchange, destinationFiat, delta, delta, true, tradeIds, proxyUrl)
                                        .searchXrapidPayments(payments, allTrades, rate);
                                });
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

        Map<String, List<Payment>> map = xrpLedgerService.fetchOdlCandidatePayments(start, end, false).stream()
                .filter(p -> p.getDestinationTag() != null && p.getDestinationTag() != 0)
                .collect(Collectors.groupingBy(p -> new StringBuilder().append(p.getSource()).append(":").append(p.getDestination()).append(":").append(p.getDestinationTag()).toString()));

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
                destinationTagRepeatService.add(sourceAddress, destinationAddress, source, destiantion, todayRepeat, destinationTag, sum);
            }
        }
    }
}
