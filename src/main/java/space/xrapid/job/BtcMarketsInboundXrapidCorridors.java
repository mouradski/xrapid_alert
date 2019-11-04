package space.xrapid.job;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import space.xrapid.domain.Exchange;
import space.xrapid.service.BtcMarketsService;
import space.xrapid.service.TradeService;

@Component
public class BtcMarketsInboundXrapidCorridors extends InboundXrapidCorridors {

    @Autowired
    private BtcMarketsService btcMarketsService;

    @Override
    protected TradeService getTradeService() {
        return btcMarketsService;
    }

    @Override
    protected int getPriority() {
        return 0;
    }

    @Override
    protected Exchange getDestinationExchange() {
        return Exchange.BTC_MARKETS;
    }
}
