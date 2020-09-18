package space.xrapid.service;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
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
public class QuidaxService implements TradeService {

  private String apiUrl = "https://www.quidax.com/api/v2/trades?market=xrpngn";

  @Override
  public List<Trade> fetchTrades(OffsetDateTime begin) {
    HttpEntity<String> entity = getEntity();

    ResponseEntity<space.xrapid.domain.quidax.Trade[]> response = restTemplate.exchange(apiUrl,
        HttpMethod.GET, entity, space.xrapid.domain.quidax.Trade[].class);

    return Arrays.stream(response.getBody())
        .map(this::mapTrade)
        .filter(filterTradePerDate(begin))
        .collect(Collectors.toList());
  }

  private Trade mapTrade(space.xrapid.domain.quidax.Trade trade) {
    OffsetDateTime date = OffsetDateTime
        .parse(trade.getCreatedAt(), DateTimeFormatter.ISO_OFFSET_DATE_TIME);

    return Trade.builder().amount(Double.valueOf(trade.getVolume()))
        .exchange(getExchange())
        .timestamp(date.toEpochSecond() * 1000)
        .dateTime(date)
        .orderId(trade.getId())
        .rate(Double.valueOf(trade.getPrice()))
        .side("sell".equals(trade.getSide()) ? "sell" : "buy")
        .build();
  }

  @Override
  public Exchange getExchange() {
    return Exchange.QUIDAX;
  }
}
