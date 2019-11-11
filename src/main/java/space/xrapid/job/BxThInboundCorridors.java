package space.xrapid.job;

import org.springframework.stereotype.Component;
import space.xrapid.domain.Exchange;

@Component
public class BxThInboundCorridors extends InboundXrapidCorridors {

    @Override
    protected Exchange getDestinationExchange() {
        return Exchange.BX_IN;
    }

    @Override
    protected int getPriority() {
        return 2;
    }
}
