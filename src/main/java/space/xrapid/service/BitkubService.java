package space.xrapid.service;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import space.xrapid.domain.Exchange;
import space.xrapid.domain.Trade;
import space.xrapid.domain.bitkub.Trades;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
public class BitkubService implements TradeService {

    private RestTemplate restTemplate = new RestTemplate();


    private String apiUrl = "https://api.bitkub.com/api/market/trades?sym=THB_XRP&lmt=10000";

    @Override
    public List<Trade> fetchTrades(OffsetDateTime begin) {
        HttpEntity<String> entity = getEntity();

        ResponseEntity<Trades> response = restTemplate.exchange(apiUrl,
                HttpMethod.GET, entity, Trades.class);


        return response.getBody().getResult().stream()
                .map(this::mapTrade)
                .filter(filterTradePerDate(begin))
                .collect(Collectors.toList());
    }

    @Override
    public Exchange getExchange() {
        return Exchange.BITKUB;
    }

    private Trade mapTrade(List<String> trade) {

        OffsetDateTime date = OffsetDateTime.ofInstant(Instant.ofEpochSecond(Long.valueOf(trade.get(0))), ZoneId.of("UTC"));


        return Trade.builder()
                .side("BUY".equals(trade.get(3)) ? "buy" : "sell")
                .timestamp(Long.valueOf(trade.get(0)) * 1000)
                .rate(Double.valueOf(trade.get(1)))
                .amount(Double.valueOf(trade.get(2)))
                .exchange(Exchange.BITKUB)
                .dateTime(date)
                .orderId(trade.get(0) + trade.get(2).replace(".", "")) // API don't provide orderId so we fill with dateTime + amount
                .build();
    }

    private Predicate<Trade> filterTradePerDate(OffsetDateTime begin) {
        return p -> begin.minusMinutes(2).isBefore(p.getDateTime());
    }
}
