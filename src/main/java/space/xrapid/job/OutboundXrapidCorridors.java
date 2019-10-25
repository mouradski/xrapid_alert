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

import javax.annotation.PostConstruct;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
public abstract class OutboundXrapidCorridors extends XrapidCorridors {

    @Autowired
    private TradeCacheService tradeCacheService;

    @Autowired
    private ExchangeToExchangePaymentService exchangeToExchangePaymentService;

    @Autowired
    private SimpMessageSendingOperations messagingTemplate;

    private DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss'Z'");


    private List<Trade> trades = new ArrayList<>();

    private List<String> allExchangeAddresses;

    @PostConstruct
    public void init() {
        allExchangeAddresses = Arrays.stream(Exchange.values()).map(e -> e.getAddresses()).flatMap(Arrays::stream)
                .collect(Collectors.toList());
    }

    public void searchXrapidPayments(List<Payment> payments) {
        trades = tradeCacheService.getCandidateOutboundTrades(getDestinationExchange());

        submit(payments);
    }

    private void submit(List<Payment> payments) {
        List<Payment> paymentsToProcess = payments.stream()
                .filter(this::isXrapidCandidate).collect(Collectors.toList());

        if (paymentsToProcess.isEmpty()) {
            return;
        }

        paymentsToProcess.stream()
                .map(this::mapPayment)
                .filter(this::fiatToXrpTradeExistOrAddressIdentified)
                .sorted(Comparator.comparing(ExchangeToExchangePayment::getDateTime))
                .forEach(this::persistPayment);

    }

    private boolean isXrapidCandidate(Payment payment) {
        return getDestinationExchange().equals(Exchange.byAddress(payment.getDestination())) && allExchangeAddresses.contains(payment.getSource());
    }



    private boolean fiatToXrpTradeExistOrAddressIdentified(ExchangeToExchangePayment exchangeToExchangePayment) {
        exchangeToExchangePayment.setDestinationCurrencry(exchangeToExchangePayment.getDestination().getLocalFiat());

        Map<OffsetDateTime, List<Trade>> aggregatedTrades  = trades.stream()
                .filter(trade -> getDestinationExchange().equals(exchangeToExchangePayment.getDestination()))
                .filter(trade -> (exchangeToExchangePayment.getDateTime().toEpochSecond() - trade.getDateTime().toEpochSecond()) >= 0)
                .filter(trade -> (exchangeToExchangePayment.getDateTime().toEpochSecond() - trade.getDateTime().toEpochSecond()) < 60)
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

            return true;
        }

        return false;
    }


    @Override
    protected List<Trade> takeClosest(ExchangeToExchangePayment exchangeToExchangePayment, List<List<Trade>> groupedXrpTrades) {

        return groupedXrpTrades.stream()
                .sorted(Comparator.comparing(tradesGroup -> getAmountDelta(exchangeToExchangePayment, (List<Trade>) tradesGroup))
                        .thenComparing(tradesGroup -> getDateDelta(exchangeToExchangePayment, (List<Trade>) tradesGroup)))
                .findFirst().get();
    }

    @Override
    protected double getDateDelta(ExchangeToExchangePayment exchangeToExchangePayment, List<Trade> tradesGroup) {
        return Double.valueOf(exchangeToExchangePayment.getTimestamp() - tradesGroup.get(0).getTimestamp());
    }

    @Override
    protected double getAmountDelta(ExchangeToExchangePayment exchangeToExchangePayment, List<Trade> tradesGroup) {
        return Double.valueOf(Math.abs(exchangeToExchangePayment.getAmount() - totalAmount(tradesGroup)));
    }


    @Override
    protected boolean amountMatches(ExchangeToExchangePayment exchangeToExchangePayment, double aggregatedAmount) {
        return exchangeToExchangePayment.getAmount().equals(aggregatedAmount);
    }
}
