package space.xrapid.job;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import space.xrapid.domain.Exchange;
import space.xrapid.service.BxService;
import space.xrapid.service.TradeService;

@Component
public class BxThCorridors extends XrapidCorridors {

    @Autowired
    private BxService bxService;

    @Override
    protected TradeService getTradeService() {
        return bxService;
    }

    @Override
    protected Exchange getDestinationExchange() {
        return Exchange.BX_IN;
    }
}
