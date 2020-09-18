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
import space.xrapid.domain.exmo.Trades;
import space.xrapid.domain.exmo.XRPUAH;

@Service
public class ExmoService implements TradeService {

  private String apiUrl = "https://api.exmo.com/v1/trades/?pair=XRP_UAH";

  @Override
  public List<Trade> fetchTrades(OffsetDateTime begin) {
    HttpEntity<String> entity = getEntity();

    ResponseEntity<Trades> response = restTemplate.exchange(apiUrl,
        HttpMethod.GET, entity, Trades.class);

    return response.getBody().getXRPUAH().stream()
        .map(this::mapTrade)
        .filter(filterTradePerDate(begin))
        .collect(Collectors.toList());
  }

  @Override
  public Exchange getExchange() {
    return Exchange.EXMO;
  }

  private Trade mapTrade(XRPUAH trade) {

    OffsetDateTime date = OffsetDateTime
        .ofInstant(Instant.ofEpochSecond(Long.valueOf(trade.getDate())), ZoneId.of("UTC"));

    return Trade.builder()
        .side(trade.getType())
        .timestamp(trade.getDate() * 1000)
        .rate(trade.getPrice())
        .amount(trade.getAmount())
        .exchange(Exchange.EXMO)
        .dateTime(date)
        .orderId(trade.getTradeId())
        .build();
  }
}
