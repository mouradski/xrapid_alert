package space.xrapid.service;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import space.xrapid.domain.Exchange;
import space.xrapid.domain.Trade;
import space.xrapid.domain.independentreserve.Trades;

@Service
public class IndependentReserveNzdService implements TradeService {

  private String apiUrl = "https://api.independentreserve.com/Public/GetRecentTrades?primaryCurrencyCode=xrp&secondaryCurrencyCode=nzd&numberOfRecentTradesToRetrieve=50";

  @Override
  public List<Trade> fetchTrades(OffsetDateTime begin) {
    HttpEntity<String> entity = getEntity();

    ResponseEntity<Trades> response = restTemplate.exchange(getApiUrl(),
        HttpMethod.GET, entity, Trades.class);

    return response.getBody().getTrades().stream()
        .map(this::mapTrade)
        .filter(filterTradePerDate(begin))
        .collect(Collectors.toList());
  }

  @Override
  public Exchange getExchange() {
    return Exchange.INDEP_RESERVE;
  }

  private Trade mapTrade(space.xrapid.domain.independentreserve.Trade trade) {
    OffsetDateTime dateTime = OffsetDateTime
        .parse(trade.getTradeTimestampUtc().replaceAll("\\..+", "+00:00"),
            DateTimeFormatter.ISO_OFFSET_DATE_TIME);

    return Trade.builder()
        .exchange(getExchange())
        .dateTime(dateTime)
        .timestamp(dateTime.toEpochSecond() * 1000)
        .amount(trade.getPrimaryCurrencyAmount())
        .side("buy")
        .rate(trade.getSecondaryCurrencyTradePrice())
        .orderId(String.valueOf(dateTime.toEpochSecond() * 1000))
        .build();
  }

  protected String getApiUrl() {
    return apiUrl;
  }
}
