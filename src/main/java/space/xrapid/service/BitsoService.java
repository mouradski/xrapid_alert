package space.xrapid.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import space.xrapid.domain.Exchange;
import space.xrapid.domain.Trade;
import space.xrapid.domain.bitso.BitsoXrpTrades;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
@Slf4j
public class BitsoService implements TradeService {

    private String url = "https://api.bitso.com/v3/trades/?book=xrp_mxn&sort=desc&limit=100";

    private DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME;

    public List<Trade> fetchTrades(OffsetDateTime begin) {
        List<Trade> payments = new ArrayList<>();
        List<Trade> currentPayments = new ArrayList<>();

        HttpEntity<String> entity = getEntity();

        ResponseEntity<BitsoXrpTrades> response = restTemplate.exchange(url,
                HttpMethod.GET, entity, BitsoXrpTrades.class);


        if (response.getBody().getSuccess() && response.getBody() != null) {
            currentPayments = getTrades(begin, response);

            payments.addAll(currentPayments);
        }

        Integer marker;

        while (currentPayments.size() == 100) {
            marker = getMarker(begin, response);

            if (marker == null) {
                break;
            }
            response = restTemplate.exchange(url + "&marker=" + marker,
                    HttpMethod.GET, entity, BitsoXrpTrades.class);

            if (response.getBody().getSuccess() && response.getBody() != null) {
                currentPayments = getTrades(begin, response);
                payments.addAll(currentPayments);
            }
        }

        return payments;
    }

    private Integer getMarker(OffsetDateTime begin, ResponseEntity<BitsoXrpTrades> response) {
        return response.getBody().getPayment().stream()
                .filter(filterBitsoTradePerDate(begin))
                .map(space.xrapid.domain.bitso.Trade::getTid)
                .sorted()
                .findFirst()
                .orElse(null);
    }

    private List<Trade> getTrades(OffsetDateTime begin, ResponseEntity<BitsoXrpTrades> response) {
        return response.getBody().getPayment().stream()
                .sorted(Comparator.comparing(space.xrapid.domain.bitso.Trade::getCreatedAt))
                .map(this::mapTrade)
                .filter(filterTradePerDate(begin))
                .collect(Collectors.toList());
    }

    private Trade mapTrade(space.xrapid.domain.bitso.Trade trade) {
        return Trade.builder().amount(Double.valueOf(trade.getAmount()))
                .exchange(Exchange.BITSO)
                .timestamp(OffsetDateTime.parse(trade.getCreatedAt().replace("0000", "00:00"), dateTimeFormatter).toEpochSecond() * 1000)
                .dateTime(OffsetDateTime.parse(trade.getCreatedAt().replace("0000", "00:00"), dateTimeFormatter))
                .orderId(trade.getTid().toString())
                .rate(Double.valueOf(trade.getPrice()))
                .side(trade.getMakerSide())
                .build();
    }

    private Predicate<space.xrapid.domain.bitso.Trade> filterBitsoTradePerDate(OffsetDateTime begin) {
        return p -> begin.isBefore(OffsetDateTime.parse(p.getCreatedAt().replace("0000", "00:00"), dateTimeFormatter));
    }

    @Override
    public Exchange getExchange() {
        return Exchange.BITSO;
    }

}
