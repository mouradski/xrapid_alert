package space.xrapid.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import space.xrapid.domain.Exchange;
import space.xrapid.domain.XrpTrade;
import space.xrapid.domain.bitso.BitsoXrpTrades;
import space.xrapid.domain.bitso.Trade;

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

    private RestTemplate restTemplate = new RestTemplate();

    private DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME;

    public List<XrpTrade> fetchTrades(OffsetDateTime begin) {
        List<XrpTrade> payments = new ArrayList<>();
        List<XrpTrade> currentPayments = new ArrayList<>();

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
                .filter(filterTradePerDate(begin))
                .map(Trade::getTid)
                .sorted()
                .findFirst()
                .orElse(null);
    }

    private List<XrpTrade> getTrades(OffsetDateTime begin, ResponseEntity<BitsoXrpTrades> response) {
        return response.getBody().getPayment().stream()
                .filter(filterTradePerDate(begin))
                .sorted(Comparator.comparing(Trade::getCreatedAt))
                .map(this::mapTrade)
                .collect(Collectors.toList());
    }

    private XrpTrade mapTrade(Trade trade) {
        return XrpTrade.builder().amount(Double.valueOf(trade.getAmount()))
                .target(Exchange.BITSO)
                .timestamp(OffsetDateTime.parse(trade.getCreatedAt().replace("0000", "00:00"), dateTimeFormatter).toEpochSecond() * 1000)
                .dateTime(OffsetDateTime.parse(trade.getCreatedAt().replace("0000", "00:00"), dateTimeFormatter))
                .orderId(trade.getTid().toString())
                .rate(Double.valueOf(trade.getPrice()))
                .build();
    }

    private Predicate<Trade> filterTradePerDate(OffsetDateTime begin) {
        return p -> begin.isBefore(OffsetDateTime.parse(p.getCreatedAt().replace("0000", "00:00"), dateTimeFormatter));
    }

}
