package space.xrapid.service;

import org.springframework.stereotype.Service;
import space.xrapid.domain.Exchange;

@Service
public class LiquidEurService  extends LiquidJpyService {
    @Override
    protected String getProductId() {
        return "85";
    }

    @Override
    public Exchange getExchange() {
        return Exchange.LIQUID_EUR;
    }
}
