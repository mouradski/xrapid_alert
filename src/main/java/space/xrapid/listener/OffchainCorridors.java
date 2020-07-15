package space.xrapid.listener;

import org.springframework.messaging.simp.SimpMessageSendingOperations;
import space.xrapid.domain.Exchange;
import space.xrapid.domain.ExchangeToExchangePayment;
import space.xrapid.domain.SpottedAt;
import space.xrapid.domain.Trade;
import space.xrapid.service.ExchangeToExchangePaymentService;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class OffchainCorridors extends XrapidCorridors {

    private Exchange source;
    private Exchange destination;


    private Set<String> fiatToXrpTradeIds;
    private Set<String> xrpToFiatTradeIds;


    public OffchainCorridors(ExchangeToExchangePaymentService exchangeToExchangePaymentService, SimpMessageSendingOperations messagingTemplate, Exchange source, Exchange destination, Set<String> fiatToXrpTradeIds, Set<String> xrpToFiatTradesIds) {
        super(exchangeToExchangePaymentService, null, null, messagingTemplate, null, null, null);

        this.source = source;
        this.destination = destination;
        this.fiatToXrpTradeIds = fiatToXrpTradeIds;
        this.xrpToFiatTradeIds = xrpToFiatTradesIds;
        this.exchangeToExchangePaymentService = exchangeToExchangePaymentService;
        this.messagingTemplate = messagingTemplate;
    }

    public void searchXrapidPayments(List<Trade> trades, double rate) {

        this.rate = rate;

        OffsetDateTime firstTradeDate = trades.stream()
            .filter(trade -> "buy".equals(trade.getSide()))
            .filter(trade -> trade.getExchange().equals(source))
            .map(Trade::getDateTime).sorted()
            .findFirst().orElse(null);

        if (firstTradeDate == null) {
            return;
        }

        final OffsetDateTime sepDate = firstTradeDate;

        for (int i = 0; i <= 90; i++) {

            final int inc = i;

            List<Trade> buyTrades = trades.stream()
                .filter(trade -> "buy".equals(trade.getSide()))
                .filter(trade -> trade.getExchange().equals(source))
                .filter(trade -> trade.getDateTime().isBefore(sepDate.plusSeconds(inc)))
                .collect(Collectors.toList());

            List<Trade> sellTTrades = trades.stream()
                .filter(trade -> "sell".equals(trade.getSide()))
                .filter(trade -> trade.getExchange().equals(destination))
                .filter(trade -> trade.getDateTime().isAfter(sepDate.plusSeconds(inc)))
                .collect(Collectors.toList());

            buyTrades.stream()
                .filter(trade -> !fiatToXrpTradeIds.contains(trade.getOrderId()))
                .collect(Collectors.groupingBy(Trade::getDateTime)).values().forEach(subBuyTrades -> {

                final long buyTimestamp = subBuyTrades.get(0).getTimestamp();
                final double buyAmount = subBuyTrades.stream().mapToDouble(Trade::getAmount).sum();

                if (buyAmount % 1 != 0 && buyAmount > 500) {
                    sellTTrades.stream()
                        .filter(trade -> !xrpToFiatTradeIds.contains(trade.getOrderId()))
                        .filter(trade -> (trade.getTimestamp() - buyTimestamp) <= 30 * 1000)

                        .collect(Collectors.groupingBy(Trade::getDateTime)).values().forEach(subSellTrades -> {

                        final double sellAmount = subSellTrades.stream().mapToDouble(Trade::getAmount).sum();

                        double diff = buyAmount - sellAmount;
                        if (diff <= 0.000001 && diff > 0) {
                            fiatToXrpTradeIds.addAll(subBuyTrades.stream().map(Trade::getOrderId).collect(Collectors.toList()));
                            xrpToFiatTradeIds.addAll(subSellTrades.stream().map(Trade::getOrderId).collect(Collectors.toList()));
                            persistPayment(buildPayment(subBuyTrades, subSellTrades, buyAmount, this.rate));
                        }
                    });
                }
            });
        }
    }

    private ExchangeToExchangePayment buildPayment(List<Trade> buy, List<Trade> sell, double amount, double rate) {
        String hash = new StringBuilder().append("0FFCHAIN_").append(sell.get(0).getTimestamp()).append("_").append(buy.get(0).getTimestamp()).toString();
        return ExchangeToExchangePayment.builder()
            .transactionHash(hash)
            .amount(amount)
            .usdValue(amount * rate)
            .dateTime(sell.get(0).getDateTime())
            .timestamp(sell.get(0).getTimestamp())
            .destination(destination)
            .source(source)
            .destinationAddress("NULL")
            .sourceAddress("NULL")
            .spottedAt(getSpottedAt())
            .destinationCurrencry(destination.getLocalFiat())
            .sourceFiat(source.getLocalFiat())
            .tag(0l)
            .inTradeFound(true)
            .outTradeFound(true)
            .fiatToXrpTrades(buy)
            .xrpToFiatTrades(sell)
            .tradeOutIds(buy.stream().map(Trade::getOrderId).collect(Collectors.joining(";")))
            .tradeIds(sell.stream().map(Trade::getOrderId).collect(Collectors.joining(";")))
            .fiatToXrpTradeIds(buy.stream().map(Trade::getOrderId).collect(Collectors.toList()))
            .xrpToFiatTradeIds(sell.stream().map(Trade::getOrderId).collect(Collectors.toList()))
            .build();
    }


    @Override
    public Exchange getSourceExchange() {
        return null;
    }

    @Override
    public SpottedAt getSpottedAt() {
        return SpottedAt.OFF_CHAIN;
    }
}
