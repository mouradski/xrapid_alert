package space.xrapid.service;

import org.springframework.stereotype.Service;

@Service
public class CoinfieldJpyService extends CoinfieldUsdService {
    @Override
    protected String getMarket() {
        return "xrpjpy";
    }
}
