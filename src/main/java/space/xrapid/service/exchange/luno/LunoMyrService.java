package space.xrapid.service.exchange.luno;

import org.springframework.stereotype.Service;
import space.xrapid.domain.Exchange;

@Service
public class LunoMyrService extends LunoService {

    @Override
    public Exchange getExchange() {
        return Exchange.LUNO_MYR;
    }

    protected String getMarket() {
        return "XRPMYR";
    }
}
