package space.xrapid.job;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import space.xrapid.domain.*;
import space.xrapid.domain.Currency;
import space.xrapid.domain.ripple.Payment;
import space.xrapid.listener.endtoend.EndToEndXrapidCorridors;
import space.xrapid.listener.inbound.InboundXrapidCorridors;
import space.xrapid.listener.outbound.OutboundXrapidCorridors;
import space.xrapid.service.*;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.*;
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
    protected SimpMessageSendingOperations messagingTemplate;

    @Autowired
    private RateService rateService;


    private OffsetDateTime lastWindowEnd;
    private OffsetDateTime windowStart;
    private OffsetDateTime windowEnd;

    @Scheduled(fixedDelay = 45000)
    public void odl() throws Exception {


        OffsetDateTime lastWindowEndRollback = lastWindowEnd;
        OffsetDateTime windowStartRollback = windowStart;
        OffsetDateTime windowEndRollback = windowEnd;

        List<Exchange> allConfirmedExchange = Stream.of(Exchange.values()).collect(Collectors.toList());
        List<Exchange> availableExchangesWithApi = tradeServices.stream().map(TradeService::getExchange).collect(Collectors.toList());

        Set<Currency> destinationFiats = availableExchangesWithApi.stream().map(Exchange::getLocalFiat).collect(Collectors.toSet());

        try {
            updatePaymentsWindows();

            List<Trade> allTrades = new ArrayList<>();

            tradeServices.stream()
                    .filter(service -> service.getExchange().isConfirmed())
                    .forEach(tradeService -> {
                        try {
                            List<Trade> trades = tradeService.fetchTrades(windowStart);
                            allTrades.addAll(trades);
                            log.info("{} trades fetched from {}", trades.size(), tradeService.getExchange());
                        } catch (Exception e) {
                            log.error("Error fetching {} trades", tradeService.getExchange());
                        }
                    });

            double rate = rateService.getXrpUsdRate();

            log.info("Fetching payments from XRP Ledger from {} to {}", windowStart.minusMinutes(12), windowEnd);
            List<Payment> payments = xrpLedgerService.fetchPayments(windowStart.minusMinutes(12), windowEnd);
            log.info("{} payments fetched from XRP Ledger", payments.size());

            // Scan all XRPL TRX between exchanges that providing API

            destinationFiats.forEach(fiat -> {
            availableExchangesWithApi.stream()
                        .filter(exchange -> !exchange.getLocalFiat().equals(fiat))
                        .forEach(exchange -> {
                            final Set<String> tradeIds = new HashSet<>();
                            Arrays.asList(30, 60, 90, 180).forEach(delta -> {
                                new EndToEndXrapidCorridors(exchangeToExchangePaymentService, xrapidInboundAddressService, messagingTemplate, exchange, fiat, delta, delta, true, tradeIds)
                                        .searchXrapidPayments(payments, allTrades, rate);
                            });

                            new EndToEndXrapidCorridors(exchangeToExchangePaymentService, xrapidInboundAddressService, messagingTemplate, exchange, fiat, 60, 60, false, tradeIds)
                                    .searchXrapidPayments(payments, allTrades, rate);
                        });
            });


            // Search all XRPL TRX between all exchanges, that are followed by a sell in the local currency (in case source exchange not providing API)
            availableExchangesWithApi.forEach(exchange -> {
                new InboundXrapidCorridors(exchangeToExchangePaymentService, messagingTemplate, exchange, availableExchangesWithApi).searchXrapidPayments(payments, allTrades.stream().filter(trade -> trade.getExchange().equals(exchange)).collect(Collectors.toList()), rate);
            });

            // Search for all XRPL TRX from exchanges with API to all exchanes (in case destination exchange not providing API)
            allConfirmedExchange.stream()
                    .filter(exchange -> !availableExchangesWithApi.contains(exchange))
                    .forEach(exchange -> {
                        new OutboundXrapidCorridors(exchangeToExchangePaymentService, messagingTemplate, exchange, availableExchangesWithApi).searchXrapidPayments(payments, allTrades, rate);
                    });


            Stats stats = exchangeToExchangePaymentService.calculateStats();

            if (stats != null) {
                messagingTemplate.convertAndSend("/topic/stats", exchangeToExchangePaymentService.calculateStats());
            }

        } catch (Exception e) {
            log.error("", e);
            lastWindowEnd = lastWindowEndRollback;
            windowStart = windowStartRollback;
            windowEnd = windowEndRollback;

            Thread.sleep(30000);
        }

    }

    private void updatePaymentsWindows() {
        windowEnd = OffsetDateTime.now(ZoneOffset.UTC);
        windowStart = windowEnd.minusMinutes(100);

        if (lastWindowEnd != null) {
            windowStart = lastWindowEnd;
        }

        lastWindowEnd = windowEnd;
    }
}
