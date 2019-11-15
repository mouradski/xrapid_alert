package space.xrapid.service;

import org.springframework.stereotype.Service;

@Service
public class KrakenEurService extends KrakenUsdService {

    protected String getPair() {
        return "XRPEUR";
    }
}
