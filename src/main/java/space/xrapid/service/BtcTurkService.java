package space.xrapid.service;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import space.xrapid.domain.Exchange;
import space.xrapid.domain.Trade;
import space.xrapid.domain.btcturk.Datum;
import space.xrapid.domain.btcturk.Trades;
import space.xrapid.domain.bx.MessageConverter;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BtcTurkService implements TradeService {

    private String apiUrl = "https://api.btcturk.com/api/v2/trades?pairSymbol=XRP_TRY&last=500";

    @Override
    public List<Trade> fetchTrades(OffsetDateTime begin) {
        HttpEntity<String> entity = getEntity();

        RestTemplate bxRestTemplate = new RestTemplate();
        bxRestTemplate.getMessageConverters().add(new MessageConverter());

        ResponseEntity<Trades> response = bxRestTemplate.exchange(apiUrl,
                HttpMethod.GET, entity, Trades.class);

        return response.getBody().getData().stream()
                .map(this::mapTrade)
                .filter(filterTradePerDate(begin))
                .collect(Collectors.toList());
    }

    @Override
    public Exchange getExchange() {
        return Exchange.BTC_TURK;
    }

    private Trade mapTrade(Datum trade) {

        OffsetDateTime date = OffsetDateTime
                .ofInstant(Instant.ofEpochSecond(Long.valueOf(trade.getDate() / 1000)), ZoneId.of("UTC"));

        return Trade.builder()
                .side(trade.getSide())
                .timestamp(trade.getDate())
                .rate(trade.getPrice())
                .amount(trade.getAmount())
                .exchange(getExchange())
                .dateTime(date)
                .orderId(trade.getTid())
                .build();
    }
}
