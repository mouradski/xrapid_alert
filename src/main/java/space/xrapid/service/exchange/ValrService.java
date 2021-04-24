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
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ValrService implements TradeService {

    private String apiUrl = "https://api.valr.com/v1/public/XRPZAR/trades?skip=0&limit=100";

    @Override
    public List<Trade> fetchTrades(OffsetDateTime begin) {
        HttpEntity<String> entity = getEntity();

        ResponseEntity<space.xrapid.domain.exchange.valr.Trade[]> response = restTemplate
                .exchange(apiUrl,
                        HttpMethod.GET, entity, space.xrapid.domain.exchange.valr.Trade[].class);


        return Arrays.stream(response.getBody())
                .map(this::mapTrade)
                .filter(filterTradePerDate(begin))
                .collect(Collectors.toList());
    }

    @Override
    public Exchange getExchange() {
        return Exchange.VALR_ZAR;
    }

    private Trade mapTrade(space.xrapid.domain.exchange.valr.Trade trade) {

        OffsetDateTime tratedAt = OffsetDateTime.parse(trade.getTradedAt().replace("Z", "+00:00"),
                DateTimeFormatter.ISO_OFFSET_DATE_TIME);

        return Trade.builder().amount(Double.valueOf(trade.getQuantity()))
                .exchange(getExchange())
                .timestamp(tratedAt.toEpochSecond() * 1000)
                .dateTime(tratedAt)
                .orderId(trade.getId())
                .rate(Double.valueOf(trade.getPrice()))
                .side(trade.getTakerSide())
                .build();
    }
}
