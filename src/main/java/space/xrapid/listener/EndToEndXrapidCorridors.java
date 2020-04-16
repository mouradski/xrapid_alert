package space.xrapid.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import space.xrapid.domain.*;
import space.xrapid.domain.ripple.Payment;
import space.xrapid.service.ExchangeToExchangePaymentService;
import space.xrapid.service.TradesFoundCacheService;
import space.xrapid.service.XrapidInboundAddressService;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static space.xrapid.job.Scheduler.transactionHashes;

@Slf4j
public class EndToEndXrapidCorridors extends XrapidCorridors {

    private Exchange destinationExchange;

    private Currency sourceFiat;

    private boolean requireEndToEnd;

    public Exchange getDestinationExchange() {
        return destinationExchange;
    }

    public Currency getSourceFiat() {
        return sourceFiat;
    }


    public EndToEndXrapidCorridors(ExchangeToExchangePaymentService exchangeToExchangePaymentService, TradesFoundCacheService tradesFoundCacheService, XrapidInboundAddressService xrapidInboundAddressService,
                                   SimpMessageSendingOperations messagingTemplate, Exchange destinationExchange, Currency sourceFiat, long buyDelta, long sellDelta, boolean requireEndToEnd, Set<String> tradeIds, String proxyUrl) {

        super(exchangeToExchangePaymentService, tradesFoundCacheService, xrapidInboundAddressService, messagingTemplate, null, tradeIds, proxyUrl);


        this.buyDelta = buyDelta;
        this.sellDelta = sellDelta;

        this.requireEndToEnd = requireEndToEnd;

        this.sourceFiat = sourceFiat;
        this.destinationExchange = destinationExchange;
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
                    .forEach(payment -> persistPayment(payment));

        } else {
            payments.stream()
                    .map(this::mapPayment)
                    .filter(payment -> this.getDestinationExchange().equals(payment.getDestination()))
                    .peek(payment -> payment.setSourceFiat(this.sourceFiat))
                    .filter(xrapidInboundAddressService::isXrapidDestination)
                    .peek(payment -> payment.setSpottedAt(SpottedAt.DESTINATION_TAG))
                    .sorted(Comparator.comparing(ExchangeToExchangePayment::getTimestamp))
                    .forEach(payment -> persistPayment(payment));
        }
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
                    .source(Exchange.byAddress(payment.getSource(), getSourceFiat()))
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
}
