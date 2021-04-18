package space.xrapid.service.exchange;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import space.xrapid.domain.Exchange;
import space.xrapid.domain.Trade;
import space.xrapid.domain.novadax.Datum;
import space.xrapid.domain.novadax.Trades;
import space.xrapid.service.TradeService;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class NovadaxService implements TradeService {

    private String apiUrl = "https://api.novadax.com/v1/market/trades?symbol=XRP_BRL&limit=1000";

    @Override
    public List<Trade> fetchTrades(OffsetDateTime begin) {
        HttpEntity<String> entity = getEntity();

        ResponseEntity<Trades> response = restTemplate.exchange(apiUrl,
                HttpMethod.GET, entity, Trades.class);

        return response.getBody().getData().stream()
                .map(this::mapTrade)
                .filter(filterTradePerDate(begin))
                .collect(Collectors.toList());
    }

    private Trade mapTrade(Datum trade) {

        OffsetDateTime date = OffsetDateTime
                .ofInstant(Instant.ofEpochSecond(Long.valueOf(trade.getTimestamp() / 1000)),
                        ZoneId.of("UTC"));

        return Trade.builder()
                .side(trade.getSide())
                .timestamp(trade.getTimestamp())
                .rate(trade.getPrice())
                .amount(trade.getAmount())
                .exchange(getExchange())
                .dateTime(date)
                // Generated ID as the api don't provide one
                .orderId(new StringBuilder(trade.getSide()).append(trade.getTimestamp()).toString())
                .build();
    }

    @Override
    public Exchange getExchange() {
        return Exchange.NOVADAX;
    }
}
