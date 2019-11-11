package space.xrapid.job;

import org.springframework.stereotype.Component;
import space.xrapid.domain.Exchange;

@Component
public class BrlInboundCorridors extends InboundXrapidCorridors {

    @Override
    protected Exchange getDestinationExchange() {
        return Exchange.MERCADO;
    }

    @Override
    protected int getPriority() {
        return 0;
    }
}
