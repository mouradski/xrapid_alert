package space.xrapid.service;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import space.xrapid.domain.Exchange;
import space.xrapid.domain.Trade;
import space.xrapid.domain.kraken.Trades;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class KrakenUsdService implements TradeService {

    private String apiUrl = "https://api.kraken.com/0/public/Trades?pair={pair}";

    @Override
    public List<Trade> fetchTrades(OffsetDateTime begin) {
        HttpEntity<String> entity = getEntity();

        ResponseEntity<Trades> response = restTemplate.exchange(apiUrl.replace("{pair}", getPair()),
                HttpMethod.GET, entity, Trades.class);

        ArrayList<List> trades = (ArrayList) response.getBody().getResult().getAdditionalProperties()
                .values()
                .stream()
                .findFirst().get();

        return trades.stream()
                .map(this::mapTrade)
                .filter(filterTradePerDate(begin))
                .collect(Collectors.toList());
    }

    @Override
    public Exchange getExchange() {
        return Exchange.KRAKEN_USD;
    }

    private Trade mapTrade(List trade) {
        OffsetDateTime date = OffsetDateTime
                .ofInstant(Instant.ofEpochSecond(Math.round(Double.valueOf(trade.get(2).toString()))),
                        ZoneId.of("UTC"));
        return Trade.builder().amount(Double.valueOf(Double.valueOf(trade.get(1).toString())))
                .exchange(getExchange())
                .timestamp(date.toEpochSecond() * 1000)
                .dateTime(date)
                .orderId(String.valueOf(trade.get(2)).replaceAll("\\.", ""))
                .rate(Double.valueOf(trade.get(0).toString()))
                .side("b".equals(trade.get(3)) ? "buy" : "sell")
                .build();
    }

    protected String getPair() {
        return "XRPUSD";
    }
}
