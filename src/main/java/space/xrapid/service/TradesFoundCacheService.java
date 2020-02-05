package space.xrapid.service;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import space.xrapid.domain.Trade;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TradesFoundCacheService {

    Map<String, List<Trade>> xrpToFiatTrades = new HashMap<>();
    Map<String, List<Trade>> fiatToXrpTrades = new HashMap<>();

    @Scheduled(fixedDelay = 300000)
    public void purge() {
        OffsetDateTime purgeDate = OffsetDateTime.now(ZoneOffset.UTC).minusHours(2);


    }

    private OffsetDateTime getTradesTime(List<Trade> trades) {
        return trades.get(0).getDateTime();
    }

    public void addXrpToFiatTrades(String trxHash, List<Trade> trades) {
        xrpToFiatTrades.put(trxHash, trades);
    }

    public void addFiatToXrpTrades(String trxHash, List<Trade> trades) {
        fiatToXrpTrades.put(trxHash, trades);
    }

    public List<Trade> getXrpToFiatTrades(String trxHash) {
        return xrpToFiatTrades.get(trxHash);
    }

    public List<Trade> getFiatToXrpTrades(String trxHash) {
        return fiatToXrpTrades.get(trxHash);
    }
}
