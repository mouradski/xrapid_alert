package space.xrapid.job;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import space.xrapid.domain.Exchange;
import space.xrapid.service.MercadoBitcoinService;
import space.xrapid.service.TradeService;

@Component
public class BrlInboundCorridors extends InboundXrapidCorridors {

    @Autowired
    private MercadoBitcoinService mercadoBitcoinService;

    @Override
    protected TradeService getTradeService() {
        return mercadoBitcoinService;
    }

    @Override
    protected Exchange getDestinationExchange() {
        return Exchange.MERCADO;
    }

    @Override
    protected int getPriority() {
        return 0;
    }
}
