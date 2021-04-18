package space.xrapid.service.exchange.coinfield;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import space.xrapid.domain.Exchange;
import space.xrapid.domain.Trade;
import space.xrapid.domain.coinfield.Trades;
import space.xrapid.service.TradeService;

@Service
public class CoinfieldUsdService implements TradeService {

  private String apiUrl = "https://api.coinfield.com/v1/trades/{market}?limit=1000&timestamp={timestamp}&order_by=desc";

  @Override
  public List<Trade> fetchTrades(OffsetDateTime begin) {
    HttpEntity<String> entity = getEntity();

    ResponseEntity<Trades> response = restTemplate.exchange(apiUrl.replace("{market}", getMarket())
            .replace("{timestamp}", String.valueOf(begin.toEpochSecond())),
        HttpMethod.GET, entity, Trades.class);

    return getTrades(begin, response);
  }

  private List<Trade> getTrades(OffsetDateTime begin, ResponseEntity<Trades> response) {
    return response.getBody().getTrades().stream()
        .sorted(Comparator.comparing(space.xrapid.domain.coinfield.Trade::getTimestamp))
        .map(this::mapTrade)
        .filter(filterTradePerDate(begin))
        .collect(Collectors.toList());
  }

  private Trade mapTrade(space.xrapid.domain.coinfield.Trade trade) {
    return Trade.builder().amount(Double.valueOf(trade.getVolume()))
        .exchange(getExchange())
        .timestamp(OffsetDateTime.parse(trade.getTimestamp().replace("Z", "+00:00"),
            DateTimeFormatter.ISO_OFFSET_DATE_TIME).toEpochSecond() * 1000)
        .dateTime(OffsetDateTime.parse(trade.getTimestamp().replace("Z", "+00:00"),
            DateTimeFormatter.ISO_OFFSET_DATE_TIME))
        .orderId(trade.getId())
        .rate(Double.valueOf(trade.getPrice()))
        //FIXME
        .side("buy")
        .build();
  }

  protected String getMarket() {
    return "xrpusd";
  }

  @Override
  public Exchange getExchange() {
    return Exchange.COINFIELD_USD;
  }

}
