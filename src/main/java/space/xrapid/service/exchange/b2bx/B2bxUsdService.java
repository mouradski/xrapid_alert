package space.xrapid.service.exchange.b2bx;

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
public class B2bxUsdService implements TradeService {

    private String apiUrl = "https://cmc-gate.b2bx.exchange/marketdata/cmc/v1/trades/XRP_QUOTE";

    @Override
    public List<Trade> fetchTrades(OffsetDateTime begin) {
        HttpEntity<String> entity = getEntity();

        ResponseEntity<space.xrapid.domain.b2bx.Trade[]> response = restTemplate
                .exchange(apiUrl.replace("QUOTE", getQuoteAsset()),
                        HttpMethod.GET, entity, space.xrapid.domain.b2bx.Trade[].class);

        return Arrays.stream(response.getBody())
                .map(this::mapTrade)
                .filter(filterTradePerDate(begin))
                .collect(Collectors.toList());
    }

    @Override
    public Exchange getExchange() {
        return Exchange.B2BX_USD;
    }

    protected String getQuoteAsset() {
        return "USD";
    }

    private Trade mapTrade(space.xrapid.domain.b2bx.Trade trade) {
        OffsetDateTime date = OffsetDateTime
                .ofInstant(Instant.ofEpochSecond(Long.valueOf(trade.getTradeTimestamp())), ZoneId.of("UTC"));
        return Trade.builder()
                .amount(trade.getBaseVolume())
                .exchange(getExchange())
                .timestamp(trade.getTradeTimestamp() * 1000)
                .dateTime(date)
                .orderId(trade.getTradeID())
                .rate(trade.getPrice())
                .side(trade.getType())
                .build();
    }
}
