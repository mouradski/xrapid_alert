package space.xrapid.service.exchange.kraken;

import org.springframework.stereotype.Service;
import space.xrapid.domain.Exchange;

@Service
public class KrakenJpyService extends KrakenUsdService {

    protected String getPair() {
        return "XRPJPY";
    }

    @Override
    public Exchange getExchange() {
        return Exchange.KRAKEN_JPY;
    }
}
