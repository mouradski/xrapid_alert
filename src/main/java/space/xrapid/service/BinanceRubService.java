package space.xrapid.service;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import space.xrapid.domain.Exchange;
import space.xrapid.domain.Trade;
import space.xrapid.domain.bitso.BitsoXrpTrades;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BinanceRubService implements TradeService {

    private String apiUrl = "https://api.binance.com/api/v3/trades?symbol=XRPRUB&limit=1000";

    @Override
    public List<Trade> fetchTrades(OffsetDateTime begin) {
        HttpEntity<String> entity = getEntity();

        ResponseEntity<space.xrapid.domain.binance.Trade[]> response = restTemplate.exchange(apiUrl,
                HttpMethod.GET, entity, space.xrapid.domain.binance.Trade[].class);

        return Arrays.stream(response.getBody())
                .map(this::mapTrade)
                .filter(filterTradePerDate(begin))
                .collect(Collectors.toList());
    }

    @Override
    public Exchange getExchange() {
        return Exchange.BINANCE_RUB;
    }

    private Trade mapTrade(space.xrapid.domain.binance.Trade trade) {

        OffsetDateTime date = OffsetDateTime.ofInstant(Instant.ofEpochMilli(trade.getTime()), ZoneId.of("UTC"));

        return Trade.builder().amount(trade.getQty())
                .exchange(Exchange.BINANCE_RUB)
                .timestamp(date.toEpochSecond() * 1000)
                .dateTime(date)
                .orderId(trade.getId().toString())
                .rate(Double.valueOf(trade.getPrice()))
                .side(trade.getIsBuyerMaker() ? "buy" : "sell")
                .build();
    }
}
