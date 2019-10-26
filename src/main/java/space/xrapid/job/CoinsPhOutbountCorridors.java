package space.xrapid.job;

import org.springframework.stereotype.Component;
import space.xrapid.domain.Exchange;

@Component
public class CoinsPhOutbountCorridors extends OutboundXrapidCorridors {

    @Override
    protected Exchange getDestinationExchange() {
        return Exchange.COIN_PH;
    }

}
