package space.xrapid.job;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import space.xrapid.domain.Exchange;
import space.xrapid.domain.ExchangeToExchangePayment;
import space.xrapid.domain.XrpTrade;
import space.xrapid.domain.ripple.Payment;
import space.xrapid.service.ExchangeToExchangePaymentService;
import space.xrapid.service.TradeService;
import space.xrapid.service.XrapidInboundAddressService;

import javax.annotation.PostConstruct;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public abstract class XrapidCorridors {

    @Autowired
    private ExchangeToExchangePaymentService exchangeToExchangePaymentService;

    @Autowired
    private SimpMessageSendingOperations messagingTemplate;

    @Autowired
    private XrapidInboundAddressService xrapidInboundAddressService;

    private List<XrpTrade> xrpTrades = new ArrayList<>();

    private DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss'Z'");

    private List<String> allExchangeAddresses;

    @PostConstruct
    public void init() {
        allExchangeAddresses = Arrays.stream(Exchange.values()).map(e -> e.getAddresses()).flatMap(Arrays::stream)
                .collect(Collectors.toList());
    }

    public void searchXrapidPayments(List<Payment> payments, OffsetDateTime windowStart) {

        if (getTradeService() != null) {
            xrpTrades = getTradeService().fetchTrades(windowStart);
        }

        submit(payments);
    }

    private void notify(ExchangeToExchangePayment payment) {
        log.info("Xrapid payment {} ", payment);
        messagingTemplate.convertAndSend("/topic/payments", payment);
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
                .filter(this::mxnXrpToCurrencyTradeExistOrAddressIdentified)
                .sorted(Comparator.comparing(ExchangeToExchangePayment::getDateTime))
                .forEach(this::persistPayment);

    }


    private void persistPayment(ExchangeToExchangePayment exchangeToFiatPayment) {
        if (exchangeToExchangePaymentService.save(exchangeToFiatPayment)) {
            notify(exchangeToFiatPayment);
        }
    }

    private boolean mxnXrpToCurrencyTradeExistOrAddressIdentified(ExchangeToExchangePayment exchangeToExchangePayment) {
        boolean destinationIdentifiedAsXrapid = xrapidInboundAddressService.isXrapidInbound(exchangeToExchangePayment.getDestinationAddress(), exchangeToExchangePayment.getTag());

        exchangeToExchangePayment.setDestinationCurrencry(exchangeToExchangePayment.getDestination().getLocalFiat());

        if (destinationIdentifiedAsXrapid) {
            log.info("destinationIdentifiedAsXrapid :)");
            return true;
        }

        XrpTrade xrpTrade = xrpTrades.stream()
                .filter(trade -> getDestinationExchange().equals(exchangeToExchangePayment.getDestination()))
                .filter(trade -> exchangeToExchangePayment.getAmount().equals(trade.getAmount()))
                .filter(trade -> (trade.getDateTime().toEpochSecond() - exchangeToExchangePayment.getDateTime().toEpochSecond()) > 1)
                .filter(trade -> (trade.getDateTime().toEpochSecond() - exchangeToExchangePayment.getDateTime().toEpochSecond()) < 180)
                .peek(trade -> xrapidInboundAddressService.add(exchangeToExchangePayment))
                .peek(trade -> log.info("Trx @ {}, Trade XRP -> {} @ {}", exchangeToExchangePayment.getDateTime(), exchangeToExchangePayment.getDestination().getLocalFiat(), trade.getDateTime()))
                .findFirst().orElse(null);

        if (xrpTrade == null) {
            return false;
        }

        exchangeToExchangePayment.setToFiatTrade(xrpTrade);

        return true;
    }

    private ExchangeToExchangePayment mapPayment(Payment payment) {
        try {
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
                    .build();
        } catch (ParseException e) {
            return null;
        }
    }

    protected abstract TradeService getTradeService();

    protected abstract Exchange getDestinationExchange();

    protected abstract int getPriority();

}
