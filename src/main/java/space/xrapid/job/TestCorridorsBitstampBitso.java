package space.xrapid.job;

import org.springframework.stereotype.Component;
import space.xrapid.domain.Currency;
import space.xrapid.domain.Exchange;

@Component
public class TestCorridorsBitstampBitso extends InboundOutboundXrapidCorridors {

    @Override
    protected Currency getSourceFiat() {
        return Currency.USD;
    }

    @Override
    protected Exchange getDestinationExchange() {
        return Exchange.BITSO;
    }
}
