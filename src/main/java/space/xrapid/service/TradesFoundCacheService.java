package space.xrapid.service;

import org.apache.commons.collections4.map.MultiKeyMap;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import space.xrapid.domain.Exchange;
import space.xrapid.domain.Trade;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;

@Service
public class TradesFoundCacheService {

    MultiKeyMap<String, List<Trade>> xrpToFiatTrades = new MultiKeyMap<>();
    MultiKeyMap<String, List<Trade>> fiatToXrpTrades = new MultiKeyMap<>();

    @Scheduled(fixedDelay = 300000)
    public void purge() {
        OffsetDateTime purgeDate = OffsetDateTime.now(ZoneOffset.UTC).minusHours(2);


    }

    private OffsetDateTime getTradesTime(List<Trade> trades) {
        return trades.get(0).getDateTime();
    }

    public void addXrpToFiatTrades(String trxHash, Exchange exchange, List<Trade> trades) {
        xrpToFiatTrades.put(trxHash, exchange.toString(), trades);
    }

    public void addFiatToXrpTrades(String trxHash, Exchange exchange, List<Trade> trades) {
        fiatToXrpTrades.put(trxHash, exchange.toString(), trades);
    }

    public List<Trade> getXrpToFiatTrades(String trxHash, Exchange exchange) {
        return xrpToFiatTrades.get(trxHash, exchange.toString());
    }

    public List<Trade> getFiatToXrpTrades(String trxHash, Exchange exchange) {
        return fiatToXrpTrades.get(trxHash, exchange.toString());
    }
}
