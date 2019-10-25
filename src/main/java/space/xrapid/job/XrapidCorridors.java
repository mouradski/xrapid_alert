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

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;

@Slf4j
public abstract class XrapidCorridors {

    @Autowired
    protected ExchangeToExchangePaymentService exchangeToExchangePaymentService;

    @Autowired
    protected SimpMessageSendingOperations messagingTemplate;

    @Autowired
    protected TradeCacheService tradeCacheService;

    private final double HUGE_TRANSACTION_THRESHOLD = 30000;
    private final double MEDIUM_TRANSACTION_THRESHOLD = 5000;
    private final double SMALL_TRANSACTION_THRESHOLD = 1000;

    private final double HUGE_TRANSACTION_TOLERANCE = 200;
    private final double MEDIUM_TRANSACTION_TOLERANCE = 5;
    private final double SMALL_TRANSACTION_TOLERANCE = 1;

    private DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss'Z'");

    protected abstract Exchange getDestinationExchange();

    protected ExchangeToExchangePayment mapPayment(Payment payment) {
        try {
            Exchange source = Exchange.byAddress(payment.getSource());
            Exchange destination = Exchange.byAddress(payment.getDestination());
            boolean xrapidCorridorConfirmed = source.isConfirmed() && destination.isConfirmed();

            return ExchangeToExchangePayment.builder()
                    .amount(Double.valueOf(payment.getDeliveredAmount()))
                    .destination(Exchange.byAddress(payment.getDestination()))
                    .source(Exchange.byAddress(payment.getSource()))
                    .sourceAddress(payment.getSource())
                    .destinationAddress(payment.getDestination())
                    .tag(payment.getDestinationTag())
                    .transactionHash(payment.getTxHash())
                    .timestamp(dateFormat.parse(payment.getExecutedTime()).getTime())
                    .dateTime(OffsetDateTime.parse(payment.getExecutedTime(), DateTimeFormatter.ISO_OFFSET_DATE_TIME))
                    .confirmed(xrapidCorridorConfirmed)
                    .build();
        } catch (ParseException e) {
            return null;
        }
    }

    protected List<Trade> takeClosest(ExchangeToExchangePayment exchangeToExchangePayment, List<List<Trade>> groupedXrpTrades) {

        return groupedXrpTrades.stream()
                .sorted(Comparator.comparing(tradesGroup -> getAmountDelta(exchangeToExchangePayment, (List<Trade>) tradesGroup))
                        .thenComparing(tradesGroup -> getDateDelta(exchangeToExchangePayment, (List<Trade>) tradesGroup)))
                .findFirst().get();
    }

    protected double getDateDelta(ExchangeToExchangePayment exchangeToExchangePayment, List<Trade> tradesGroup) {
        return Double.valueOf(tradesGroup.get(0).getTimestamp() - exchangeToExchangePayment.getTimestamp());
    }

    protected double getAmountDelta(ExchangeToExchangePayment exchangeToExchangePayment, List<Trade> tradesGroup) {
        return Double.valueOf(Math.abs(exchangeToExchangePayment.getAmount() - totalAmount(tradesGroup)));
    }

    protected double totalAmount(List<Trade> trades) {
        return trades.stream().mapToDouble(Trade::getAmount).sum();
    }

    protected boolean amountMatches(ExchangeToExchangePayment exchangeToExchangePayment, double aggregatedAmount) {
        return (exchangeToExchangePayment.getAmount() > HUGE_TRANSACTION_THRESHOLD && Math.abs(exchangeToExchangePayment.getAmount() - aggregatedAmount) < HUGE_TRANSACTION_TOLERANCE)
                || (exchangeToExchangePayment.getAmount() > MEDIUM_TRANSACTION_THRESHOLD && Math.abs(exchangeToExchangePayment.getAmount() - aggregatedAmount) < MEDIUM_TRANSACTION_TOLERANCE)
                || (exchangeToExchangePayment.getAmount() > SMALL_TRANSACTION_THRESHOLD && Math.abs(exchangeToExchangePayment.getAmount() - aggregatedAmount) < SMALL_TRANSACTION_TOLERANCE)
                || exchangeToExchangePayment.getAmount().equals(aggregatedAmount);
    }

    protected void persistPayment(ExchangeToExchangePayment exchangeToFiatPayment) {
        if (exchangeToExchangePaymentService.save(exchangeToFiatPayment)) {
            notify(exchangeToFiatPayment);
        }
    }

    protected void notify(ExchangeToExchangePayment payment) {
        log.info("Xrapid payment {} ", payment);
        messagingTemplate.convertAndSend("/topic/payments", payment);
    }

}
