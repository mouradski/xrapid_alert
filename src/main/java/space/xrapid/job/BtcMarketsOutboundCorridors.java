package space.xrapid.job;

import org.springframework.stereotype.Component;
import space.xrapid.domain.Exchange;

//@Component
public class BtcMarketsOutboundCorridors extends OutboundXrapidCorridors {
    @Override
    protected Exchange getDestinationExchange() {
        return Exchange.BTC_MARKETS;
    }
}
