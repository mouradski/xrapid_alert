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
import space.xrapid.domain.coinone.CompleteOrder;
import space.xrapid.domain.coinone.Trades;

@Service
public class CoinoneService implements TradeService {

  private final String apiUrl = "https://api.coinone.co.kr/trades?currency=XRP";

  @Override
  public List<Trade> fetchTrades(OffsetDateTime begin) {
    HttpEntity<String> entity = getEntity();

    ResponseEntity<Trades> response = restTemplate.exchange(apiUrl,
        HttpMethod.GET, entity, Trades.class);

    return response.getBody().getCompleteOrders().stream()
        .map(this::mapTrade)
        .filter(filterTradePerDate(begin))
        .collect(Collectors.toList());
  }

  @Override
  public Exchange getExchange() {
    return Exchange.COINONE;
  }

  private Trade mapTrade(CompleteOrder trade) {

    OffsetDateTime date = OffsetDateTime
        .ofInstant(Instant.ofEpochSecond(Long.valueOf(trade.getTimestamp())), ZoneId.of("UTC"));

    return Trade.builder()
        .side("0".equals(trade.getIsAsk()) ? "sell" : "buy")
        .timestamp(trade.getTimestamp() * 1000)
        .rate(trade.getPrice())
        .amount(trade.getQty())
        .exchange(getExchange())
        .dateTime(date)
        .orderId(trade.getId())
        .build();
  }
}
