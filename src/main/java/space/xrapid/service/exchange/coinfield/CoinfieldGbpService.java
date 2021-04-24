package space.xrapid.service.exchange.coinfield;

import org.springframework.stereotype.Service;
import space.xrapid.domain.Exchange;

@Service
public class CoinfieldGbpService extends CoinfieldUsdService {

    @Override
    protected String getMarket() {
        return "xrpgbp";
    }

    @Override
    public Exchange getExchange() {
        return Exchange.COINFIELD_GBP;
    }
}
