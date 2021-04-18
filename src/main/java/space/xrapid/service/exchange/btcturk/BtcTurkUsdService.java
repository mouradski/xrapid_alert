package space.xrapid.service.exchange.btcturk;

import org.springframework.stereotype.Service;
import space.xrapid.domain.Exchange;

@Service
public class BtcTurkUsdService extends BtcTurkService {
    private String apiUrl = "https://api.btcturk.com/api/v2/trades?pairSymbol=XRP_USD&last=500";

    @Override
    public Exchange getExchange() {
        return Exchange.BTC_TURK_USD;
    }

    protected String getApiUrl() {
        return apiUrl;
    }
}
