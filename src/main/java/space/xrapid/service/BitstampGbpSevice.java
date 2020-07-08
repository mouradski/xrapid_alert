package space.xrapid.service;

import org.springframework.stereotype.Service;
import space.xrapid.domain.Exchange;

@Service
public class BitstampGbpSevice extends BitstampUsdService {

    public BitstampGbpSevice() {
        this.apiUrl = "https://www.bitstamp.net/api/v2/transactions/xrpgbp";
    }

    @Override
    public Exchange getExchange() {
        return Exchange.BITSTAMP_GBP;
    }
}
