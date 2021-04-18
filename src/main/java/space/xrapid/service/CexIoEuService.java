package space.xrapid.service;

import org.springframework.stereotype.Service;
import space.xrapid.domain.Exchange;

@Service
public class CexIoEuService extends CexIoUsdService {

    @Override
    public Exchange getExchange() {
        return Exchange.CEX_IO_EUR;
    }

    public String getMarket() {
        return "EUR";
    }
}
