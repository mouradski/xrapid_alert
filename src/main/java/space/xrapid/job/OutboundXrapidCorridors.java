package space.xrapid.job;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import space.xrapid.domain.ExchangeToExchangePayment;
import space.xrapid.domain.Trade;
import space.xrapid.domain.ripple.Payment;
import space.xrapid.service.TradeCacheService;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
public abstract class OutboundXrapidCorridors extends XrapidCorridors {

    @Autowired
    private TradeCacheService tradeCacheService;

    public void searchXrapidPayments(List<Payment> payments) {
        trades = tradeCacheService.getCandidateOutboundTrades(getDestinationExchange());

        submit(payments);
    }

    @Override
    protected Map<OffsetDateTime, List<Trade>> getAggregatedTrades(ExchangeToExchangePayment exchangeToExchangePayment) {
        return trades.stream()
                    .filter(trade -> getDestinationExchange().equals(exchangeToExchangePayment.getDestination()))
                    .filter(trade -> (exchangeToExchangePayment.getDateTime().toEpochSecond() - trade.getDateTime().toEpochSecond()) >= 0)
                    .filter(trade -> (exchangeToExchangePayment.getDateTime().toEpochSecond() - trade.getDateTime().toEpochSecond()) < 60)
                    .collect(Collectors.groupingBy(Trade::getDateTime));
    }

    @Override
    protected double getDateDelta(ExchangeToExchangePayment exchangeToExchangePayment, List<Trade> tradesGroup) {
        return Double.valueOf(exchangeToExchangePayment.getTimestamp() - tradesGroup.get(0).getTimestamp());
    }

    @Override
    protected double getAmountDelta(ExchangeToExchangePayment exchangeToExchangePayment, List<Trade> tradesGroup) {
        return Double.valueOf(Math.abs(exchangeToExchangePayment.getAmount() - totalAmount(tradesGroup)));
    }
}
