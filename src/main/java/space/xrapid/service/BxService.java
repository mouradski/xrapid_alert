package space.xrapid.service;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import space.xrapid.domain.Exchange;
import space.xrapid.domain.Trade;
import space.xrapid.domain.bx.MessageConverter;
import space.xrapid.domain.bx.Response;

import javax.annotation.PostConstruct;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BxService implements TradeService {

    private String apiUrl = "https://bx.in.th/api/trade/?pairing=xrp";

    @PostConstruct
    private void init() {
        //BX.IN.TH send non Json contentType even we set Accept header = Json
        restTemplate.getMessageConverters().add(new MessageConverter());
    }

    @Override
    public List<Trade> fetchTrades(OffsetDateTime begin) {
        HttpEntity<String> entity = getEntity();

        ResponseEntity<Response> response = restTemplate.exchange(apiUrl,
                HttpMethod.GET, entity, Response.class);

        return response.getBody().getTrades().stream()
                .map(this::mapTrade)
                .filter(filterTradePerDate(begin))
                .collect(Collectors.toList());
    }


    private Trade mapTrade(space.xrapid.domain.bx.Trade trade) {
        //TODO check if exchange using UTC
        OffsetDateTime date = OffsetDateTime.parse(trade.getTradeDate().replace(" ", "T" ) + "+00:00",
                DateTimeFormatter.ISO_DATE_TIME);

        return Trade.builder().amount(Double.valueOf(trade.getAmount()))
                .exchange(Exchange.BX_IN)
                .timestamp(date.toEpochSecond() * 1000)
                .dateTime(date)
                .orderId(trade.getOrderId())
                .rate(Double.valueOf(trade.getRate()))
                .build();
    }

    @Override
    public Exchange getExchange() {
        return Exchange.BX_IN;
    }
}
