package space.xrapid.service.exchange.liquid;

import org.springframework.stereotype.Service;
import space.xrapid.domain.Exchange;

@Service
public class LiquidSgdService extends LiquidJpyService {

    @Override
    protected String getProductId() {
        return "86";
    }

    @Override
    public Exchange getExchange() {
        return Exchange.LIQUID_SGD;
    }


}
