package space.xrapid.job;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import space.xrapid.domain.Currency;
import space.xrapid.domain.Exchange;
import space.xrapid.domain.ExchangeToExchangePayment;
import space.xrapid.domain.Trade;
import space.xrapid.domain.ripple.Payment;
import space.xrapid.listener.endtoend.EndToEndXrapidCorridors;
import space.xrapid.listener.inbound.InboundXrapidCorridors;
import space.xrapid.listener.outbound.OutboundXrapidCorridors;
import space.xrapid.service.ExchangeToExchangePaymentService;
import space.xrapid.service.RateService;
import space.xrapid.service.TradeService;
import space.xrapid.service.XrpLedgerService;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
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
    protected SimpMessageSendingOperations messagingTemplate;

    @Autowired
    private RateService rateService;



    private OffsetDateTime lastWindowEnd;
    private OffsetDateTime windowStart;
    private OffsetDateTime windowEnd;

    @Scheduled(fixedDelay = 30000)
    public void odl() throws Exception {

        OffsetDateTime lastWindowEndRollback = lastWindowEnd;
        OffsetDateTime windowStartRollback = windowStart;
        OffsetDateTime windowEndRollback = windowEnd;

        List<Exchange> allConfirmedExchange = Stream.of(Exchange.values()).filter(exchange -> exchange.isConfirmed()).collect(Collectors.toList());
        List<Exchange> availableExchangesWithApi = tradeServices.stream().map(TradeService::getExchange).collect(Collectors.toList());

        List<Currency> destinationFiats = availableExchangesWithApi.stream().map(Exchange::getLocalFiat).collect(Collectors.toList());

        try {
            updatePaymentsWindows();

            List<Trade> allTrades = new ArrayList<>();

             tradeServices.forEach(tradeService -> {
                 allTrades.addAll(tradeService.fetchTrades(windowStart));
            });


            double rate = rateService.getXrpUsdRate();

            log.info("Fetching payments from XRP Ledger from {} to {}", windowStart.minusMinutes(5), windowEnd);
            List<Payment> payments = xrpLedgerService.fetchPayments(windowStart.minusMinutes(5), windowEnd);
            log.info("{} payments fetched from XRP Ledger", payments.size());

            // Scan all XRPL TRX between exchanges that providing API
            List<CompletableFuture<List<ExchangeToExchangePayment>>> endToEndFeatures = new ArrayList<>();
            destinationFiats.forEach(fiat -> {
                availableExchangesWithApi.stream()
                        .filter(exchange -> !exchange.getLocalFiat().equals(fiat))
                        .forEach(exchange -> {
                            new EndToEndXrapidCorridors(exchangeToExchangePaymentService, messagingTemplate, exchange, fiat).searchXrapidPayments(payments, allTrades, rate);
                        });
            });



            // Search all XRPL TRX between all exchanges, that are followed by a sell in the local currency (in case source exchange not providing API)
            List<CompletableFuture<List<ExchangeToExchangePayment>>> inboundFeatures = new ArrayList<>();
            availableExchangesWithApi.forEach(exchange -> {
                new InboundXrapidCorridors(exchangeToExchangePaymentService, messagingTemplate, exchange, availableExchangesWithApi).searchXrapidPayments(payments, allTrades.stream().filter(trade -> trade.getExchange().equals(exchange)).collect(Collectors.toList()), rate);
            });

            // Search for all XRPL TRX from exchanges with API to all exchanes (in case destination exchange not providing API)
            List<CompletableFuture<List<ExchangeToExchangePayment>>> outboundFeatures = new ArrayList<>();
            allConfirmedExchange.stream()
                    .filter(exchange -> !availableExchangesWithApi.contains(exchange))
                    .forEach(exchange -> {
                        new OutboundXrapidCorridors(exchangeToExchangePaymentService, messagingTemplate, exchange, availableExchangesWithApi).searchXrapidPayments(payments, allTrades, rate);
                    });


            messagingTemplate.convertAndSend("/topic/stats", exchangeToExchangePaymentService.calculateStats());

        } catch (Exception e) {
            log.error("", e);
            lastWindowEnd = lastWindowEndRollback;
            windowStart = windowStartRollback;
            windowEnd = windowEndRollback;

            Thread.sleep(120000);
        }

    }

    private void updatePaymentsWindows() {
        windowEnd = OffsetDateTime.now(ZoneOffset.UTC);
        windowStart = windowEnd.minusMinutes(800);

        if (lastWindowEnd != null) {
            windowStart = lastWindowEnd;
        }

        lastWindowEnd = windowEnd;
    }
}
