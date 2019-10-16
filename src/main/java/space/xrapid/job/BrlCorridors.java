package space.xrapid.job;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;
import space.xrapid.service.MercadoBitcoinService;
import space.xrapid.service.TradeService;

@Slf4j
@EnableScheduling
@Component
public class BrlCorridors extends XrapidCorridors {

    @Autowired
    private MercadoBitcoinService mercadoBitcoinService;

    @Override
    protected TradeService getTradeService() {
        return mercadoBitcoinService;
    }
}
