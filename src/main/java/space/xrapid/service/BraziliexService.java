package space.xrapid.service;

import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import space.xrapid.domain.Exchange;
import space.xrapid.domain.XrpTrade;
import space.xrapid.domain.braziliex.Trade;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BraziliexService implements TradeService {

    private String apiUrl = "https://braziliex.com/api/v1/public/tradehistory/xrp_brl/{TIMESTAMP}";

    private RestTemplate restTemplate = new RestTemplate();

    @Override
    public List<XrpTrade> fetchTrades(OffsetDateTime begin) {
        String urlGet = apiUrl.replace("{TIMESTAMP}", String.valueOf(OffsetDateTime.now(ZoneOffset.UTC).toEpochSecond() * 1000));

        ResponseEntity<Trade[]> response = restTemplate.exchange(urlGet,
                HttpMethod.GET, getEntity(), Trade[].class);

        return getTrades(begin, response);
    }

    private List<XrpTrade> getTrades(OffsetDateTime begin, ResponseEntity<space.xrapid.domain.braziliex.Trade[]> response) {
        long beginTimestamp = begin.toEpochSecond() * 1000;
        return Arrays.stream(response.getBody())
                .filter(p -> beginTimestamp < p.getTimestamp())
                .sorted(Comparator.comparing(Trade::getTimestamp))
                .peek(System.out::println)
                .map(this::mapTrade)
                .collect(Collectors.toList());
    }

    private XrpTrade mapTrade(Trade trade) {
        return XrpTrade.builder().amount(Double.valueOf(trade.getAmount())).target(Exchange.BRAZILIEX).build();
    }
}
