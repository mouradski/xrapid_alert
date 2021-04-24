package space.xrapid.service.exchange.cexio;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import space.xrapid.domain.Exchange;
import space.xrapid.domain.Trade;
import space.xrapid.domain.exchange.cexio.MessageConverter;
import space.xrapid.service.TradeService;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class CexIoUsdService implements TradeService {

    private String apiUrl = "https://cex.io/api/trade_history/XRP/{market}";

    @Override
    public List<Trade> fetchTrades(OffsetDateTime begin) {
        HttpEntity<String> entity = getEntity();

        RestTemplate cexRestTemplate = new RestTemplate();
        cexRestTemplate.getMessageConverters().add(new MessageConverter());

        ResponseEntity<space.xrapid.domain.exchange.cexio.Trade[]> response = cexRestTemplate
                .exchange(apiUrl.replace("{market}", getMarket()),
                        HttpMethod.GET, entity, space.xrapid.domain.exchange.cexio.Trade[].class);

        return getTrades(begin, response);
    }

    private List<Trade> getTrades(OffsetDateTime begin,
                                  ResponseEntity<space.xrapid.domain.exchange.cexio.Trade[]> response) {
        return Stream.of(response.getBody())
                .sorted(Comparator.comparing(space.xrapid.domain.exchange.cexio.Trade::getDate))
                .map(this::mapTrade)
                .filter(filterTradePerDate(begin))
                .collect(Collectors.toList());
    }

    private Trade mapTrade(space.xrapid.domain.exchange.cexio.Trade trade) {
        OffsetDateTime date = OffsetDateTime
                .ofInstant(Instant.ofEpochSecond(trade.getDate()), ZoneId.of("UTC"));
        return Trade.builder().amount(Double.valueOf(trade.getAmount()))
                .exchange(getExchange())
                .timestamp(date.toEpochSecond() * 1000)
                .dateTime(date)
                .orderId(trade.getTid())
                .rate(Double.valueOf(trade.getPrice()))
                .side(trade.getType())
                .build();
    }

    @Override
    public Exchange getExchange() {
        return Exchange.CEX_IO;
    }

    public String getMarket() {
        return "USD";
    }
}
