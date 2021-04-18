package space.xrapid.service.exchange.liquid;

import org.springframework.stereotype.Service;
import space.xrapid.domain.Exchange;

@Service
public class LiquidUsdService extends LiquidJpyService {
    @Override
    protected String getProductId() {
        return "84";
    }

    @Override
    public Exchange getExchange() {
        return Exchange.LIQUID_USD;
    }
}
