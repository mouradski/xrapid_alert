package space.xrapid.service.exchange;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import space.xrapid.domain.Exchange;
import space.xrapid.domain.Trade;
import space.xrapid.service.TradeService;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BitfinexService implements TradeService {

    private final String apiUrl = "https://api-pub.bitfinex.com/v2/trades/tXRPUSD/hist?limit=1000";

    @Override
    public List<Trade> fetchTrades(OffsetDateTime begin) {
        HttpEntity<String> entity = getEntity();

        ResponseEntity<List> response = restTemplate.exchange(apiUrl,
                HttpMethod.GET, entity, List.class);

        return (List) response.getBody().stream()
                .map(trade -> mapTrade((List) trade))
                .filter(filterTradePerDate(begin))
                .collect(Collectors.toList());
    }

    @Override
    public Exchange getExchange() {
        return Exchange.BITFINEX;
    }

    private Trade mapTrade(List trade) {

        long timestamp = Long.valueOf(Long.valueOf(trade.get(1).toString()));

        OffsetDateTime date = OffsetDateTime
                .ofInstant(Instant.ofEpochSecond(timestamp / 1000), ZoneId.of("UTC"));

        double amount = Double.valueOf(trade.get(2).toString());
        double price = Double.valueOf(trade.get(3).toString());

        return Trade.builder()
                .side(amount > 0 ? "buy" : "sell")
                .timestamp(timestamp)
                .rate(price)
                .amount(Math.abs(amount))
                .exchange(getExchange())
                .dateTime(date)
                .orderId(trade.get(0).toString())
                .build();
    }
}
