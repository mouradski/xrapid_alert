package space.xrapid.job;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import space.xrapid.domain.Exchange;
import space.xrapid.service.BitstampUsdService;
import space.xrapid.service.TradeService;

@Component
public class BitstampUsdCorridors extends InboundXrapidCorridors {

    @Autowired
    private BitstampUsdService bitstampUsdService;

    @Override
    protected TradeService getTradeService() {
        return bitstampUsdService;
    }

    @Override
    protected Exchange getDestinationExchange() {
        return Exchange.BITSTAMP;
    }

    @Override
    protected int getPriority() {
        return 0;
    }
}
