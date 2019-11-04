package space.xrapid.service;

import org.springframework.stereotype.Service;

@Service
public class BitstampEurSevice extends BitstampUsdService {

    public BitstampEurSevice() {
        this.apiUrl = "https://www.bitstamp.net/api/v2/transactions/xrpeur/";
    }
}
