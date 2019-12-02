package space.xrapid.service;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import space.xrapid.domain.Exchange;
import space.xrapid.domain.Trade;
import space.xrapid.domain.bitcointrade.Response;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
public class BitcoinTradeService implements TradeService {

    private String url = "https://api.bitcointrade.com.br/v2/public/BRLXRP/trades?start_time={start_time}&page_size=1000&current_page={current_page}";

    @Override
    public List<Trade> fetchTrades(OffsetDateTime begin) {

        HttpEntity entity = getEntity();

        Integer currentPage = 1;

        boolean remainingTrades = true;

        List<Trade> trades = new ArrayList<>();

        while (remainingTrades) {
            ResponseEntity<Response> response = restTemplate.exchange(url
                            .replace("{start_time}", convertDateForUrl(begin))
                            .replace("{current_page}", currentPage.toString()),
                    HttpMethod.GET, entity, Response.class);

            trades.addAll(
                    response.getBody().getData().getTrades().stream()
                            .map(this::mapTrade)
                            .filter(filterTradePerDate(begin))
                            .collect(Collectors.toList())
            );

            remainingTrades = currentPage < response.getBody().getData().getPagination().getTotalPages();

            currentPage++;
        }

        return trades;
    }

    @Override
    public Exchange getExchange() {
        return Exchange.BITCOIN_TRADE;
    }

    private Trade mapTrade(space.xrapid.domain.bitcointrade.Trade trade) {
        String stringDate = transformDate(trade.getDate());
        OffsetDateTime date = OffsetDateTime.parse(stringDate, DateTimeFormatter.ISO_OFFSET_DATE_TIME);


        return Trade.builder()
                .dateTime(date)
                .timestamp(date.toEpochSecond() * 1000)
                .amount(trade.getAmount())
                .exchange(Exchange.BITCOIN_TRADE)
                .rate(trade.getUnitPrice())
                .orderId(trade.getActiveOrderCode())
                .side(trade.getType())
                .build();
    }

    private String convertDateForUrl(OffsetDateTime date) {
        return date.toString().replaceAll("\\.[0-9]{3}Z", "-00:00");
    }

    private String transformDate(String date) {
        return date.replaceAll("\\..+", "+00:00");
    }
}
