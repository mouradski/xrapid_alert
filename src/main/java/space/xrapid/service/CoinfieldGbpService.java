package space.xrapid.service;

import org.springframework.stereotype.Service;

@Service
public class CoinfieldGbpService extends CoinfieldUsdService {
    @Override
    protected String getMarket() {
        return "xrpgbp";
    }
}
