package space.xrapid.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import space.xrapid.domain.Exchange;
import space.xrapid.domain.XrpTrade;
import space.xrapid.domain.mercadobitcoin.Trade;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class MercadoBitcoinService implements TradeService {

    private String apiUrl = "https://www.mercadobitcoin.net/api/BTC/trades/{FROM}/{TO}/";

    private RestTemplate restTemplate = new RestTemplate();

    @Override
    public List<XrpTrade> fetchTrades(OffsetDateTime begin) {
        HttpEntity<String> entity = getEntity();

        ResponseEntity<Trade[]> response = restTemplate.exchange(apiUrl.replace("{FROM}", begin.toEpochSecond() + "")
                        .replace("{TO}", OffsetDateTime.now(ZoneOffset.UTC).toEpochSecond() + ""),
                HttpMethod.GET, entity, Trade[].class);


        return Arrays.stream(response.getBody()).map(this::mapTrade).collect(Collectors.toList());
    }


    private XrpTrade mapTrade(Trade trade) {
        return XrpTrade.builder().amount(Double.valueOf(trade.getAmount()))
                .target(Exchange.MERCADO).timestamp(trade.getDate() * 1000)
                .dateTime(OffsetDateTime.ofInstant(Instant.ofEpochSecond(trade.getDate()), ZoneId.of("UTC")))
                .orderId(trade.getTid().toString())
                .rate(Double.valueOf(trade.getPrice()))
                .build();
    }

}
