package space.xrapid.job;

import org.springframework.stereotype.Component;
import space.xrapid.domain.Exchange;

//@Component
public class BitsoOutboundCorridors extends OutboundXrapidCorridors {
    @Override
    protected Exchange getDestinationExchange() {
        return Exchange.BITSO;
    }
}
