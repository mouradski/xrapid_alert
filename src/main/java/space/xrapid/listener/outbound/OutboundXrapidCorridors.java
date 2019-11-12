package space.xrapid.listener.outbound;

import lombok.extern.slf4j.Slf4j;
import space.xrapid.domain.Exchange;
import space.xrapid.domain.ExchangeToExchangePayment;
import space.xrapid.domain.SpottedAt;
import space.xrapid.domain.Trade;
import space.xrapid.domain.ripple.Payment;
import space.xrapid.listener.XrapidCorridors;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class OutboundXrapidCorridors extends XrapidCorridors {

    private Exchange destinationExchange;

    public OutboundXrapidCorridors(Exchange destinationExchange) {
        this.destinationExchange = destinationExchange;
    }

    public void searchXrapidPayments(List<Payment> payments, List<Trade> trades, double rate) {
        this.rate = rate;
        this.trades = trades;
        submit(payments);
    }

    @Override
    protected void submit(List<Payment> payments) {
        List<Payment> paymentsToProcess = payments.stream()
                .filter(this::isXrapidCandidate).collect(Collectors.toList());

        if (paymentsToProcess.isEmpty()) {
            return;
        }

        paymentsToProcess.stream()
                .map(this::mapPayment)
                .filter(this::fiatToXrpTradesExists)
                .sorted(Comparator.comparing(ExchangeToExchangePayment::getDateTime))
                .forEach(this::persistPayment);
    }

    @Override
    public Exchange getDestinationExchange() {
        return destinationExchange;
    }

    @Override
    public SpottedAt getSpottedAt() {
        return SpottedAt.SOURCE;
    }
}
