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
public class CurrencyComService implements TradeService {

    private String apiUrl = "https://api-adapter.backend.currency.com/api/v1/aggTrades?symbol=XRP/BYN";

    @Override
    public List<Trade> fetchTrades(OffsetDateTime begin) {
        HttpEntity<String> entity = getEntity();

        ResponseEntity<space.xrapid.domain.currencycom.Trade[]> response = restTemplate.exchange(apiUrl,
                HttpMethod.GET, entity, space.xrapid.domain.currencycom.Trade[].class);

        return Arrays.stream(response.getBody())
                .map(this::mapTrade)
                .collect(Collectors.toList());
    }

    @Override
    public Exchange getExchange() {
        return Exchange.CURRENCY_COM;
    }

    private Trade mapTrade(space.xrapid.domain.currencycom.Trade trade) {
        OffsetDateTime date = OffsetDateTime.ofInstant(Instant.ofEpochSecond(trade.getT()), ZoneId.of("UTC"));

        return Trade.builder()
                .dateTime(date)
                .timestamp(date.toEpochSecond() * 1000)
                .amount(trade.getQ())
                .exchange(Exchange.BITCOIN_TRADE)
                .rate(trade.getP())
                .orderId(trade.getA())
                .side(trade.getM() ? "buy" : "sell")
                .build();
    }
}
