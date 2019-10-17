package space.xrapid.job;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import space.xrapid.domain.Exchange;
import space.xrapid.service.BitstampEurSevice;
import space.xrapid.service.TradeService;

@Component
public class BitstampEurCorridors extends XrapidCorridors {

    @Autowired
    private BitstampEurSevice bitstampEurSevice;

    @Override
    protected TradeService getTradeService() {
        return bitstampEurSevice;
    }

    @Override
    protected Exchange getDestinationExchange() {
        return Exchange.BITSTAMP;
    }
}
