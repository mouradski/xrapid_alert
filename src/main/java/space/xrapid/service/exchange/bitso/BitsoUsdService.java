package space.xrapid.service.exchange.bitso;

import org.springframework.stereotype.Service;
import space.xrapid.domain.Exchange;

@Service
public class BitsoUsdService extends BitsoService {

    @Override
    public Exchange getExchange() {
        return Exchange.BITSO_USD;
    }

    @Override
    protected String getMarket() {
        return "xrp_usd";
    }
}
