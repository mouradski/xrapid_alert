package space.xrapid.job;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import space.xrapid.domain.Exchange;
import space.xrapid.service.BitsoService;
import space.xrapid.service.TradeService;

@Component
public class BitsoCorridors extends InboundXrapidCorridors {

    @Autowired
    private BitsoService bitsoService;

    @Override
    protected TradeService getTradeService() {
        return bitsoService;
    }

    @Override
    protected Exchange getDestinationExchange() {
        return Exchange.BITSO;
    }

    @Override
    protected int getPriority() {
        return 1;
    }
}
