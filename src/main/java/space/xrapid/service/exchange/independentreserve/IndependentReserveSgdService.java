package space.xrapid.service.exchange.independentreserve;

import org.springframework.stereotype.Service;
import space.xrapid.domain.Exchange;

@Service
public class IndependentReserveSgdService extends IndependentReserveNzdService {
    private String apiUrl = "https://api.independentreserve.com/Public/GetRecentTrades?primaryCurrencyCode=xrp&secondaryCurrencyCode=sgd&numberOfRecentTradesToRetrieve=50";


    @Override
    public Exchange getExchange() {
        return Exchange.INDEP_RESERVE_SGD;
    }


    @Override
    public String getApiUrl() {
        return apiUrl;
    }
}
