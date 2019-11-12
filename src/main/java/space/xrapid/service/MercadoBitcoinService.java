package space.xrapid.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import space.xrapid.domain.Exchange;
import space.xrapid.domain.Trade;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

//@Service
@Slf4j
public class MercadoBitcoinService implements TradeService {

    private String apiUrl = "https://www.mercadobitcoin.net/api/XRP/trades/{FROM}/{TO}/";

    private RestTemplate restTemplate = new RestTemplate();

    @Override
    public List<Trade> fetchTrades(OffsetDateTime begin) {
        HttpEntity<String> entity = getEntity();

        ResponseEntity<space.xrapid.domain.mercadobitcoin.Trade[]> response = restTemplate.exchange(apiUrl.replace("{FROM}", begin.toEpochSecond() + "")
                        .replace("{TO}", OffsetDateTime.now(ZoneOffset.UTC).toEpochSecond() + ""),
                HttpMethod.GET, entity, space.xrapid.domain.mercadobitcoin.Trade[].class);


        return Arrays.stream(response.getBody())
                .map(this::mapTrade)
                .filter(p -> begin.minusMinutes(2).isBefore(p.getDateTime()))
                .collect(Collectors.toList());
    }


    private Trade mapTrade(space.xrapid.domain.mercadobitcoin.Trade trade) {
        return Trade.builder().amount(Double.valueOf(trade.getAmount()))
                .exchange(Exchange.MERCADO)
                .timestamp(trade.getDate() * 1000)
                .dateTime(OffsetDateTime.ofInstant(Instant.ofEpochSecond(trade.getDate()), ZoneId.of("UTC")))
                .orderId(trade.getTid().toString())
                .rate(Double.valueOf(trade.getPrice()))
                .build();
    }

    @Override
    public Exchange getExchange() {
        return Exchange.MERCADO;
    }

}
