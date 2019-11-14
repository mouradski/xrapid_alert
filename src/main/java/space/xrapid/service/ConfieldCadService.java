package space.xrapid.service;

import org.springframework.stereotype.Service;

@Service
public class ConfieldCadService extends CoinfieldUsdService {
    @Override
    protected String getMarket() {
        return "xrpcad";
    }
}
