package space.xrapid.job;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import space.xrapid.domain.Exchange;
import space.xrapid.service.BitkubService;
import space.xrapid.service.TradeService;

@Component
public class BitkubInboundCorridor extends InboundXrapidCorridors {

    @Autowired
    private BitkubService bitkubService;

    @Override
    protected TradeService getTradeService() {
        return bitkubService;
    }

    @Override
    protected int getPriority() {
        return 5;
    }

    @Override
    protected Exchange getDestinationExchange() {
        return Exchange.BITKUB;
    }
}
