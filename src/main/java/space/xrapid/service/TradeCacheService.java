package space.xrapid.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import space.xrapid.domain.Exchange;
import space.xrapid.domain.Trade;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class TradeCacheService {

    List<Trade> cache = new ArrayList<>();

    public List<Trade> getCandidateOutboundTrades(Exchange targetExchange) {
        return cache.stream()
                .filter(trade -> !targetExchange.equals(trade.getExchange()))
                .collect(Collectors.toList());
    }

    public void reset() {
        cache.clear();
    }

    public void fill(List<Trade> trades) {
        cache.addAll(trades);
    }
}
