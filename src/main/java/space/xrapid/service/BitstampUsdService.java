package space.xrapid.service;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import space.xrapid.domain.Exchange;
import space.xrapid.domain.Trade;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BitstampUsdService implements TradeService {

    private RestTemplate restTemplate = new RestTemplate();

    protected String apiUrl = "https://www.bitstamp.net/api/v2/transactions/xrpusd/";

    @Override
    public List<Trade> fetchTrades(OffsetDateTime begin) {
        HttpEntity<String> entity = getEntity();

        ResponseEntity<space.xrapid.domain.bitstamp.Trade[]> response = restTemplate.exchange(apiUrl,
                HttpMethod.GET, entity, space.xrapid.domain.bitstamp.Trade[].class);

        return Arrays.stream(response.getBody())
                .map(this::mapTrade)
                .filter(p -> begin.plusMinutes(-2).isBefore(p.getDateTime()))
                .collect(Collectors.toList());
    }

    private Trade mapTrade(space.xrapid.domain.bitstamp.Trade trade) {
        OffsetDateTime date = OffsetDateTime.ofInstant(Instant.ofEpochSecond(trade.getDate()), ZoneId.of("UTC"));
        return Trade.builder().amount(Double.valueOf(trade.getAmount()))
                .target(Exchange.BITSTAMP).timestamp(date.toEpochSecond() * 1000)
                .dateTime(date)
                .orderId(trade.getTid())
                .rate(Double.valueOf(trade.getPrice()))
                .build();
    }

    @Override
    public Exchange getExchange() {
        return Exchange.BITSTAMP;
    }
}
