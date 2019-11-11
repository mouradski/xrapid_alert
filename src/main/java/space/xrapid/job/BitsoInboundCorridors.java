package space.xrapid.job;

import org.springframework.stereotype.Component;
import space.xrapid.domain.Exchange;

@Component
public class BitsoInboundCorridors extends InboundXrapidCorridors {

    @Override
    protected Exchange getDestinationExchange() {
        return Exchange.BITSO;
    }

    @Override
    protected int getPriority() {
        return 1;
    }
}
