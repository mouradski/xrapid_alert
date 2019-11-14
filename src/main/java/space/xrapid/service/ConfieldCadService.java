package space.xrapid.service;

import org.springframework.stereotype.Service;
import space.xrapid.domain.Exchange;

@Service
public class ConfieldCadService extends CoinfieldUsdService {
    @Override
    protected String getMarket() {
        return "xrpcad";
    }

    @Override
    public Exchange getExchange() {
        return Exchange.COINFIELD_CAD;
    }
}
