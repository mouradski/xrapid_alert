package space.xrapid.job;

import org.springframework.stereotype.Component;
import space.xrapid.domain.Exchange;
import space.xrapid.service.TradeService;

@Component
public class CoinsPhCorridors extends InboundXrapidCorridors {
    @Override
    protected TradeService getTradeService() {
        return null;
    }

    @Override
    protected Exchange getDestinationExchange() {
        return Exchange.COIN_PH;
    }

    @Override
    protected int getPriority() {
        return 5;
    }
}
