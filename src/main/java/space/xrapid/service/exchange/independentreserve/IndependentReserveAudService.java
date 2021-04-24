package space.xrapid.service.exchange.independentreserve;

import org.springframework.stereotype.Service;
import space.xrapid.domain.Exchange;

@Service
public class IndependentReserveAudService extends IndependentReserveNzdService {

    private String apiUrl = "https://api.independentreserve.com/Public/GetRecentTrades?primaryCurrencyCode=xrp&secondaryCurrencyCode=aud&numberOfRecentTradesToRetrieve=50";


    @Override
    public Exchange getExchange() {
        return Exchange.INDEP_RESERVE_AUD;
    }


    @Override
    public String getApiUrl() {
        return apiUrl;
    }

}
