package space.xrapid.service.exchange.luno;

import org.springframework.stereotype.Service;
import space.xrapid.domain.Exchange;

@Service
public class LunoNgnService extends LunoService {

    @Override
    public Exchange getExchange() {
        return Exchange.LUNO_NGN;
    }

    protected String getMarket() {
        return "XRPNGN";
    }
}
