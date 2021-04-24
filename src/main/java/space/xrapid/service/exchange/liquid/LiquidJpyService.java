package space.xrapid.service.exchange.liquid;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import space.xrapid.domain.Exchange;
import space.xrapid.domain.Trade;
import space.xrapid.domain.exchange.liquid.Trades;
import space.xrapid.service.TradeService;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class LiquidJpyService implements TradeService {

    private String apiUrl = "https://api.liquid.com/executions?product_id=_PRODUCT_&limit=1000";

    @Override
    public List<Trade> fetchTrades(OffsetDateTime begin) {
        HttpEntity<String> entity = getEntity();

        ResponseEntity<Trades> response = restTemplate.exchange(apiUrl.replace("_PRODUCT_", getProductId()),
                HttpMethod.GET, entity, Trades.class);

        return response.getBody().getTrades().stream()
                .map(this::mapTrade)
                .filter(filterTradePerDate(begin))
                .collect(Collectors.toList());
    }

    protected String getProductId() {
        return "83";
    }

    @Override
    public Exchange getExchange() {
        return Exchange.LIQUID;
    }

    private Trade mapTrade(space.xrapid.domain.exchange.liquid.Trade trade) {
        OffsetDateTime date = OffsetDateTime
                .ofInstant(Instant.ofEpochSecond(Long.valueOf(trade.getCreatedAt())), ZoneId.of("UTC"));
        return Trade.builder()
                .orderId(trade.getId())
                .side(trade.getTakerSide())
                .amount(trade.getQuantity())
                .rate(trade.getPrice())
                .dateTime(date)
                .timestamp(trade.getCreatedAt() * 1000)
                .exchange(getExchange())
                .build();
    }
}
