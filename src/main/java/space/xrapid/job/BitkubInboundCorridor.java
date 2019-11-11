package space.xrapid.job;

import org.springframework.stereotype.Component;
import space.xrapid.domain.Exchange;

@Component
public class BitkubInboundCorridor extends InboundXrapidCorridors {

    @Override
    protected int getPriority() {
        return 5;
    }

    @Override
    protected Exchange getDestinationExchange() {
        return Exchange.BITKUB;
    }
}
