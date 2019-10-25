package space.xrapid.job;

import lombok.extern.slf4j.Slf4j;
import space.xrapid.domain.ripple.Payment;
import space.xrapid.service.TradeService;

import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.List;

@Slf4j
public abstract class InboundXrapidCorridors extends XrapidCorridors {


    public void searchXrapidPayments(List<Payment> payments, OffsetDateTime windowStart) {

        tradesIdAlreadyProcessed = new HashSet<>();

        if (getTradeService() != null) {
            trades = getTradeService().fetchTrades(windowStart);
            tradeCacheService.fill(trades);
        }

        submit(payments);
    }


    protected abstract TradeService getTradeService();

    protected abstract int getPriority();
}
