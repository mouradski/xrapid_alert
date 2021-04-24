package space.xrapid.service.exchange.coinfield;

import org.springframework.stereotype.Service;
import space.xrapid.domain.Exchange;

@Service
public class CoinfieldAedService extends CoinfieldUsdService {

    @Override
    protected String getMarket() {
        return "xrpaed";
    }

    @Override
    public Exchange getExchange() {
        return Exchange.COINFIELD_AED;
    }
}
