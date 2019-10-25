package space.xrapid.job;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import space.xrapid.domain.Exchange;
import space.xrapid.domain.ExchangeToExchangePayment;
import space.xrapid.domain.Trade;
import space.xrapid.domain.ripple.Payment;
import space.xrapid.service.ExchangeToExchangePaymentService;
import space.xrapid.service.TradeCacheService;
import space.xrapid.service.TradeService;

import javax.annotation.PostConstruct;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
public abstract class InboundXrapidCorridors extends XrapidCorridors {

    private List<Trade> trades = new ArrayList<>();


    private List<String> allExchangeAddresses;
    private Set<String> tradesIdAlreadyProcessed = new HashSet<>();

    @PostConstruct
    public void init() {
        allExchangeAddresses = Arrays.stream(Exchange.values()).map(e -> e.getAddresses()).flatMap(Arrays::stream)
                .collect(Collectors.toList());
    }

    public void searchXrapidPayments(List<Payment> payments, OffsetDateTime windowStart) {
       
        tradesIdAlreadyProcessed = new HashSet<>();
        
        if (getTradeService() != null) {
            trades = getTradeService().fetchTrades(windowStart);
            tradeCacheService.fill(trades);
        }

        submit(payments);
    }

    private boolean isXrapidCandidate(Payment payment) {
        return getDestinationExchange().equals(Exchange.byAddress(payment.getDestination())) && allExchangeAddresses.contains(payment.getSource());
    }

    private void submit(List<Payment> payments) {
        List<Payment> paymentsToProcess = payments.stream()
                .filter(this::isXrapidCandidate).collect(Collectors.toList());

        if (paymentsToProcess.isEmpty()) {
            return;
        }

        paymentsToProcess.stream()
                .map(this::mapPayment)
                .filter(this::xrpToFiatTradeExistOrAddressIdentified)
                .sorted(Comparator.comparing(ExchangeToExchangePayment::getDateTime))
                .forEach(this::persistPayment);

    }

    private boolean xrpToFiatTradeExistOrAddressIdentified(ExchangeToExchangePayment exchangeToExchangePayment) {
        exchangeToExchangePayment.setDestinationCurrencry(exchangeToExchangePayment.getDestination().getLocalFiat());

        Map<OffsetDateTime, List<Trade>> aggregatedTrades  = trades.stream()
                .filter(trade -> getDestinationExchange().equals(exchangeToExchangePayment.getDestination()))
                .filter(trade -> (trade.getDateTime().toEpochSecond() - exchangeToExchangePayment.getDateTime().toEpochSecond()) >= 0)
                .filter(trade -> (trade.getDateTime().toEpochSecond() - exchangeToExchangePayment.getDateTime().toEpochSecond()) < 60)
                .filter(trade -> !tradesIdAlreadyProcessed.contains(trade.getOrderId()))
                .collect(Collectors.groupingBy(Trade::getDateTime));


        List<List<Trade>> candidates = new ArrayList<>();

        for (Map.Entry<OffsetDateTime, List<Trade>> e : aggregatedTrades.entrySet()) {
            double amount = e.getValue().stream().mapToDouble(Trade::getAmount).sum();

            if (amountMatches(exchangeToExchangePayment, amount)) {
                candidates.add(e.getValue());
            }
        }

        if (!candidates.isEmpty()) {

            List<Trade> trades = takeClosest(exchangeToExchangePayment, candidates);

            exchangeToExchangePayment.setToFiatTrades(trades);

            String tradeIds = trades.stream().map(Trade::getOrderId).collect(Collectors.joining(";"));

            exchangeToExchangePayment.setTradeIds(tradeIds);

            tradesIdAlreadyProcessed.addAll(trades.stream().map(Trade::getOrderId).collect(Collectors.toList()));

            return true;
        }

        return false;
    }

    protected abstract TradeService getTradeService();

    protected abstract int getPriority();
}
