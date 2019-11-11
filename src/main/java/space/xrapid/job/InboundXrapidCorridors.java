package space.xrapid.job;

import lombok.extern.slf4j.Slf4j;
import space.xrapid.domain.SpottedAt;
import space.xrapid.domain.Trade;
import space.xrapid.domain.ripple.Payment;

import java.util.HashSet;
import java.util.List;

@Slf4j
public abstract class InboundXrapidCorridors extends XrapidCorridors {

    public void searchXrapidPayments(List<Payment> payments, List<Trade> trades, double rate) {

        this.rate = rate;

        this.trades = trades;

        tradesIdAlreadyProcessed = new HashSet<>();

        tradeCacheService.fill(trades);

        submit(payments);

    }

    protected abstract int getPriority();

    @Override
    protected SpottedAt getSpottedAt() {
        return SpottedAt.DESTINATION;
    }
}
