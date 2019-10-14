package space.xrapid.job;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;
import space.xrapid.service.BitsoService;
import space.xrapid.service.TradeService;

@Slf4j
@EnableScheduling
@Component
public class MxnCorridors extends XrapidCorridors {

    @Autowired
    private BitsoService bitsoService;

    @Override
    protected TradeService getTradeService() {
        return bitsoService;
    }
}
