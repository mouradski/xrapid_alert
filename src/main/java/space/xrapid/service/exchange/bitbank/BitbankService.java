package space.xrapid.service.exchange.bitbank;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import space.xrapid.domain.Exchange;
import space.xrapid.domain.Trade;
import space.xrapid.domain.exchange.bitbank.Transaction;
import space.xrapid.domain.exchange.bitbank.Transactions;
import space.xrapid.service.TradeService;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.stream.Collectors;


@Service
@Slf4j
public class BitbankService implements TradeService {

    private String apiUrl = "https://public.bitbank.cc/xrp_jpy/transactions";


    @Override
    public List<Trade> fetchTrades(OffsetDateTime begin) {
        HttpEntity<String> entity = getEntity();

        ResponseEntity<Transactions> response = restTemplate.exchange(apiUrl,
                HttpMethod.GET, entity, Transactions.class);

        return response.getBody().getData().getTransactions().stream()
                .map(this::mapTrade)
                .filter(filterTradePerDate(begin))
                .collect(Collectors.toList());
    }

    @Override
    public Exchange getExchange() {
        return Exchange.BITBANK;
    }

    private Trade mapTrade(Transaction trade) {

        OffsetDateTime date = OffsetDateTime
                .ofInstant(Instant.ofEpochSecond(Long.valueOf(trade.getExecutedAt() / 1000)),
                        ZoneId.of("UTC"));

        return Trade.builder()
                .side(trade.getSide())
                .timestamp(trade.getExecutedAt())
                .rate(trade.getPrice())
                .amount(trade.getAmount())
                .exchange(getExchange())
                .dateTime(date)
                .orderId(trade.getTransactionId().toString())
                .build();
    }
}
