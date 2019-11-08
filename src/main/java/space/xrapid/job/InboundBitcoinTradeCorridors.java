package space.xrapid.job;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import space.xrapid.domain.Exchange;
import space.xrapid.service.BitcoinTradeService;
import space.xrapid.service.TradeService;

@Component
public class InboundBitcoinTradeCorridors extends InboundXrapidCorridors {

    @Autowired
    private BitcoinTradeService bitcoinTradeService;

    @Override
    protected TradeService getTradeService() {
        return bitcoinTradeService;
    }

    @Override
    protected int getPriority() {
        return 0;
    }

    @Override
    protected Exchange getDestinationExchange() {
        return Exchange.BITCOIN_TRADE;
    }
}
