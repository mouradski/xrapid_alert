package space.xrapid.service.exchange;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import space.xrapid.domain.Exchange;
import space.xrapid.domain.Trade;
import space.xrapid.service.TradeService;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UpbitService implements TradeService {

    private String apiUrl = "https://api.upbit.com/v1/trades/ticks?market=KRW-XRP&count=500";

    @Override
    public List<Trade> fetchTrades(OffsetDateTime begin) {
        HttpEntity<String> entity = getEntity();

        ResponseEntity<space.xrapid.domain.upbit.Trade[]> response = restTemplate.exchange(apiUrl,
                HttpMethod.GET, entity, space.xrapid.domain.upbit.Trade[].class);

        return Arrays.stream(response.getBody())
                .map(this::mapTrade)
                .filter(filterTradePerDate(begin))
                .collect(Collectors.toList());
    }

    @Override
    public Exchange getExchange() {
        return Exchange.UPBIT;
    }

    private Trade mapTrade(space.xrapid.domain.upbit.Trade trade) {

        OffsetDateTime date = OffsetDateTime
                .ofInstant(Instant.ofEpochSecond(trade.getTimestamp() / 1000), ZoneId.of("UTC"));

        return Trade.builder()
                .side("ASK".equals(trade.getAskBid()) ? "buy" : "sell")
                .timestamp(trade.getTimestamp())
                .rate(trade.getTradePrice())
                .amount(trade.getTradeVolume())
                .exchange(getExchange())
                .dateTime(date)
                .orderId(trade.getSequentialId())
                .build();
    }
}
