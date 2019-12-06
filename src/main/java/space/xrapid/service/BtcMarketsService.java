package space.xrapid.service;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import space.xrapid.domain.Exchange;
import space.xrapid.domain.Trade;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BtcMarketsService implements TradeService {

    private String url = "https://api.btcmarkets.net/v3/markets/XRP-AUD/trades";

    @Override
    public List<Trade> fetchTrades(OffsetDateTime begin) {

        HttpEntity<String> entity = getEntity();

        ResponseEntity<space.xrapid.domain.btcmarkets.Trade[]> response = restTemplate.exchange(url,
                HttpMethod.GET, entity, space.xrapid.domain.btcmarkets.Trade[].class);

        return Arrays.stream(response.getBody())
                .map(this::mapTrade)
                .filter(filterTradePerDate(begin))
                .collect(Collectors.toList());
    }

    @Override
    public Exchange getExchange() {
        return Exchange.BTC_MARKETS;
    }


    private Trade mapTrade(space.xrapid.domain.btcmarkets.Trade trade) {

        String stringDate = transformDate(trade.getTimestamp());
        OffsetDateTime date = OffsetDateTime.parse(stringDate, DateTimeFormatter.ISO_OFFSET_DATE_TIME);


        return Trade.builder()
                .orderId(trade.getId())
                .side("Ask".equals(trade.getSide()) ? "buy" : "sell")
                .dateTime(date)
                .timestamp(date.toEpochSecond() * 1000)
                .amount(trade.getAmount())
                .rate(trade.getPrice())
                .exchange(Exchange.BTC_MARKETS)
                .build();
    }

    private String transformDate(String date) {
        return date.replaceAll("\\..+", "+00:00");
    }
}
