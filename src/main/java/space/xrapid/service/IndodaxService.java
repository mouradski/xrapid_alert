package space.xrapid.service;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import space.xrapid.domain.Exchange;
import space.xrapid.domain.Trade;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class IndodaxService implements TradeService {

    protected String apiUrl = "https://indodax.com/api/xrp_idr/trades";

    @Override
    public List<Trade> fetchTrades(OffsetDateTime begin) {
        HttpEntity<String> entity = getEntity();

        ResponseEntity<space.xrapid.domain.indodax.Trade[]> response = restTemplate.exchange(apiUrl,
                HttpMethod.GET, entity, space.xrapid.domain.indodax.Trade[].class);

        return Arrays.stream(response.getBody())
                .map(this::mapTrade)
                .filter(filterTradePerDate(begin))
                .collect(Collectors.toList());
    }

    @Override
    public Exchange getExchange() {
        return Exchange.INDODAX;
    }

    private Trade mapTrade(space.xrapid.domain.indodax.Trade trade) {

        OffsetDateTime date = OffsetDateTime.ofInstant(Instant.ofEpochSecond(trade.getDate()), ZoneId.of("UTC"));

        return Trade.builder().amount(Double.valueOf(trade.getAmount()))
                .exchange(getExchange())
                .timestamp(date.toEpochSecond() * 1000)
                .dateTime(date)
                .orderId(trade.getTid())
                .rate(Double.valueOf(trade.getPrice()))
                .side(trade.getType())
                .build();
    }
}
