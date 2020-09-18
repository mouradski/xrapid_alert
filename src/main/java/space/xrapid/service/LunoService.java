package space.xrapid.service;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import space.xrapid.domain.Exchange;
import space.xrapid.domain.Trade;
import space.xrapid.domain.luno.Trades;

@Service
public class LunoService implements TradeService {

  private String apiUrl = "https://api.luno.com/api/1/trades?pair=XRPZAR";

  @Override
  public List<space.xrapid.domain.Trade> fetchTrades(OffsetDateTime begin) {
    HttpEntity<String> entity = getEntity();

    ResponseEntity<Trades> response = restTemplate.exchange(apiUrl,
        HttpMethod.GET, entity, Trades.class);

    return response.getBody().getTrades().stream()
        .map(this::mapTrade)
        .filter(filterTradePerDate(begin))
        .collect(Collectors.toList());
  }

  @Override
  public Exchange getExchange() {
    return Exchange.LUNO;
  }

  private Trade mapTrade(space.xrapid.domain.luno.Trade trade) {

    OffsetDateTime date = OffsetDateTime
        .ofInstant(Instant.ofEpochSecond(Long.valueOf(trade.getTimestamp() / 1000)),
            ZoneId.of("UTC"));

    String side = trade.getIsBuy() ? "buy" : "sell";

    return Trade.builder()
        .side(side)
        .timestamp(trade.getTimestamp())
        .rate(trade.getPrice())
        .amount(trade.getVolume())
        .exchange(getExchange())
        .dateTime(date)
        // Generated ID as the api don't provide one
        .orderId(new StringBuilder(side).append("_").append(trade.getTimestamp()).toString())
        .build();
  }
}
