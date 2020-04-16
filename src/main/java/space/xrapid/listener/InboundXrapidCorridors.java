package space.xrapid.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import space.xrapid.domain.Exchange;
import space.xrapid.domain.SpottedAt;
import space.xrapid.domain.Trade;
import space.xrapid.domain.ripple.Payment;
import space.xrapid.service.ExchangeToExchangePaymentService;
import space.xrapid.service.TradesFoundCacheService;

import java.util.HashSet;
import java.util.List;

@Slf4j
public class InboundXrapidCorridors extends XrapidCorridors {

    private Exchange destinationExchange;

    public InboundXrapidCorridors(ExchangeToExchangePaymentService exchangeToExchangePaymentService, TradesFoundCacheService tradesFoundCacheService, SimpMessageSendingOperations messagingTemplate, Exchange destinationExchange, List<Exchange> exchangesWithApi, String proxyUrl) {
        super(exchangeToExchangePaymentService, tradesFoundCacheService, null, messagingTemplate, exchangesWithApi, null, proxyUrl);
        this.destinationExchange = destinationExchange;
    }

    public void searchXrapidPayments(List<Payment> payments, List<Trade> trades, double rate) {

        this.rate = rate;

        this.trades = trades;

        tradesIdAlreadyProcessed = new HashSet<>();

        submit(payments);
    }

    @Override
    public Exchange getDestinationExchange() {
        return destinationExchange;
    }

    @Override
    public SpottedAt getSpottedAt() {
        return SpottedAt.DESTINATION;
    }
}
