package space.xrapid.service;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import space.xrapid.domain.Exchange;
import space.xrapid.domain.Trade;

@Service
public class BitstampUsdService implements TradeService {

  protected String apiUrl = "https://www.bitstamp.net/api/v2/transactions/xrpusd";

  private boolean firstCall = true;

  @Override
  public List<Trade> fetchTrades(OffsetDateTime begin) {
    HttpEntity<String> entity = getEntity();

    ResponseEntity<space.xrapid.domain.bitstamp.Trade[]> response = restTemplate
        .exchange(firstCall ? apiUrl + "?time=day" : apiUrl,
            HttpMethod.GET, entity, space.xrapid.domain.bitstamp.Trade[].class);

    firstCall = false;

    return Arrays.stream(response.getBody())
        .map(this::mapTrade)
        .filter(filterTradePerDate(begin))
        .collect(Collectors.toList());
  }

  private Trade mapTrade(space.xrapid.domain.bitstamp.Trade trade) {
    OffsetDateTime date = OffsetDateTime
        .ofInstant(Instant.ofEpochSecond(trade.getDate()), ZoneId.of("UTC"));
    return Trade.builder().amount(Double.valueOf(trade.getAmount()))
        .exchange(getExchange())
        .timestamp(date.toEpochSecond() * 1000)
        .dateTime(date)
        .orderId(trade.getTid())
        .rate(Double.valueOf(trade.getPrice()))
        .side("0".equals(trade.getType()) ? "buy" : "sell")
        .build();
  }

  @Override
  public Exchange getExchange() {
    return Exchange.BITSTAMP;
  }
}
