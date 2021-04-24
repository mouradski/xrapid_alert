package space.xrapid.service.exchange;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import space.xrapid.domain.Exchange;
import space.xrapid.domain.Trade;
import space.xrapid.service.TradeService;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class WazirxService implements TradeService {

    private String apiUrl = "https://api.wazirx.com/api/v2/trades?market=xrpinr";

    @Override
    public List<Trade> fetchTrades(OffsetDateTime begin) {
        HttpEntity<String> entity = getEntity();

        ResponseEntity<space.xrapid.domain.exchange.wazirx.Trade[]> response = restTemplate.exchange(apiUrl,
                HttpMethod.GET, entity, space.xrapid.domain.exchange.wazirx.Trade[].class);

        return Stream.of(response.getBody())
                .map(this::mapTrade)
                .filter(filterTradePerDate(begin))
                .collect(Collectors.toList());
    }

    private Trade mapTrade(space.xrapid.domain.exchange.wazirx.Trade trade) {
        return Trade.builder().amount(trade.getVolume())
                .exchange(Exchange.WAZIRX)
                .timestamp(
                        OffsetDateTime.parse(trade.getCreatedAt(), DateTimeFormatter.ISO_OFFSET_DATE_TIME)
                                .toEpochSecond() * 1000)
                .dateTime(
                        OffsetDateTime.parse(trade.getCreatedAt(), DateTimeFormatter.ISO_OFFSET_DATE_TIME))
                .orderId(trade.getId().toString())
                .rate(Double.valueOf(trade.getPrice()))
                .side("buy".equals(trade.getSide()) ? "buy" : "sell")
                .build();
    }

    @Override
    public Exchange getExchange() {
        return Exchange.WAZIRX;
    }
}
