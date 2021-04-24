package space.xrapid.service.exchange;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import space.xrapid.domain.Exchange;
import space.xrapid.domain.Trade;
import space.xrapid.domain.exchange.bithumb.Datum;
import space.xrapid.domain.exchange.bithumb.Trades;
import space.xrapid.service.TradeService;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BithumbService implements TradeService {

    private String apiUrl = "https://api.bithumb.com/public/transaction_history/XRP_KRW?count=100";

    @Override
    public List<Trade> fetchTrades(OffsetDateTime begin) {
        HttpEntity<String> entity = getEntity();

        ResponseEntity<Trades> response = restTemplate.exchange(apiUrl,
                HttpMethod.GET, entity, Trades.class);

        return response.getBody().getData().stream()
                .map(this::mapTrade)
                .collect(Collectors.toList());
    }

    private Trade mapTrade(Datum trade) {

        OffsetDateTime date = OffsetDateTime
                .parse(trade.getTransactionDate().replace(" ", "T") + "+09:00",
                        DateTimeFormatter.ISO_DATE_TIME);

        return Trade.builder()
                .amount(trade.getUnitsTraded())
                .dateTime(date)
                .timestamp(date.toEpochSecond() * 1000)
                .exchange(getExchange())
                .rate(trade.getPrice())
                .side("ask".equals(trade.getType()) ? "sell" : "buy")
                .orderId(date.toEpochSecond() + "_" + trade.getTotal())
                .build();
    }

    @Override
    public Exchange getExchange() {
        return Exchange.BITHUMB;
    }
}
