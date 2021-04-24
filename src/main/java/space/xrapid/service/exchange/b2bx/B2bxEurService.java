package space.xrapid.service.exchange.b2bx;

import org.springframework.stereotype.Service;
import space.xrapid.domain.Exchange;

@Service
public class B2bxEurService extends B2bxUsdService {

    @Override
    public Exchange getExchange() {
        return Exchange.B2BX_EUR;
    }

    protected String getQuoteAsset() {
        return "EUR";
    }
}
