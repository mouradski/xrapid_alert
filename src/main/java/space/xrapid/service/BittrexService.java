package space.xrapid.service;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import space.xrapid.domain.Exchange;
import space.xrapid.domain.Trade;
import space.xrapid.domain.bittrex.Trades;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
public class BittrexService implements TradeService {

    private String apiUrl = "https://api.bittrex.com/api/v1.1/public/getmarkethistory?market=USD-XRP&limit=10";

    private RestTemplate restTemplate = new RestTemplate();

    @Override
    public List<Trade> fetchTrades(OffsetDateTime begin) {
        HttpEntity<String> entity = getEntity();

        ResponseEntity<Trades> response = restTemplate.exchange(apiUrl,
                HttpMethod.GET, entity, space.xrapid.domain.bittrex.Trades.class);

        return response.getBody().getResult().stream()
                .map(this::mapTrade)
                .filter(filterTradePerDate(begin))
                .collect(Collectors.toList());
    }

    @Override
    public Exchange getExchange() {
        return Exchange.BITTREX;
    }

    private Trade mapTrade(space.xrapid.domain.bittrex.Trade trade) {
        OffsetDateTime dateTime;

        if (trade.getTimeStamp().contains(".")) {
            dateTime = OffsetDateTime.parse(trade.getTimeStamp().replaceAll("\\.[0-9]+", "+00:00"), DateTimeFormatter.ISO_OFFSET_DATE_TIME);
        } else {
            dateTime = OffsetDateTime.parse(trade.getTimeStamp() + "+00:00", DateTimeFormatter.ISO_OFFSET_DATE_TIME);
        }

        return Trade.builder().amount(Double.valueOf(trade.getQuantity()))
                .exchange(Exchange.BITTREX)
                .timestamp(dateTime.toEpochSecond() * 1000)
                .dateTime(dateTime)
                .orderId(trade.getId().toString())
                .rate(Double.valueOf(trade.getPrice()))
                .side(trade.getOrderType().toLowerCase())
                .build();
    }

    private Predicate<Trade> filterTradePerDate(OffsetDateTime begin) {
        return trade -> begin.minusMinutes(2).isBefore(trade.getDateTime());
    }
}
