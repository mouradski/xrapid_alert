package space.xrapid.job;

import org.springframework.stereotype.Component;
import space.xrapid.domain.Exchange;

@Component
public class BitstampInboundEurCorridors extends InboundXrapidCorridors {

    @Override
    protected Exchange getDestinationExchange() {
        return Exchange.BITSTAMP;
    }

    @Override
    protected int getPriority() {
        return 0;
    }
}
