package space.xrapid.service.exchange;

import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import space.xrapid.domain.Exchange;
import space.xrapid.domain.Trade;
import space.xrapid.service.TradeService;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BraziliexService implements TradeService {

    private String apiUrl = "https://braziliex.com/api/v1/public/tradehistory/xrp_brl/{TIMESTAMP}";

    @Override
    public List<Trade> fetchTrades(OffsetDateTime begin) {
        String urlGet = apiUrl.replace("{TIMESTAMP}",
                String.valueOf(OffsetDateTime.now(ZoneOffset.UTC).toEpochSecond() * 1000));

        ResponseEntity<space.xrapid.domain.braziliex.Trade[]> response = restTemplate.exchange(urlGet,
                HttpMethod.GET, getEntity(), space.xrapid.domain.braziliex.Trade[].class);

        return getTrades(begin, response);
    }

    private List<Trade> getTrades(OffsetDateTime begin,
                                  ResponseEntity<space.xrapid.domain.braziliex.Trade[]> response) {
        long beginTimestamp = begin.toEpochSecond() * 1000;
        return Arrays.stream(response.getBody())
                .filter(p -> beginTimestamp < p.getTimestamp())
                .sorted(Comparator.comparing(space.xrapid.domain.braziliex.Trade::getTimestamp))
                .map(this::mapTrade)
                .collect(Collectors.toList());
    }

    private Trade mapTrade(space.xrapid.domain.braziliex.Trade trade) {
        return Trade.builder().amount(Double.valueOf(trade.getAmount())).exchange(Exchange.BRAZILIEX)
                .build();
    }

    @Override
    public Exchange getExchange() {
        return Exchange.BRAZILIEX;
    }
}
