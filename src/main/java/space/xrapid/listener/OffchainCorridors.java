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

    private SimpMessageSendingOperations messagingTemplate;
    private ExchangeToExchangePaymentService exchangeToExchangePaymentService;

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
            .filter(trade -> trade.getExchange().equals(source)
            ).map(Trade::getDateTime).sorted()
            .findFirst().orElse(null);

        if (firstTradeDate == null) {
            return;
        }

        final OffsetDateTime sepDate = firstTradeDate;

        for (int i=0; i<=600; i++) {


            final int inc = i;

            List<Trade> buyTrades = trades.stream()
                .filter(trade -> "buy".equals(trade.getSide()))
                .filter(trade -> trade.getExchange().equals(source))
                .filter(trade -> trade.getDateTime().isBefore(sepDate.plusSeconds(inc)))
                .collect(Collectors.toList());

            List<Trade> sellTTrades = trades.stream()
                .filter(trade -> "sell".equals(trade.getSide()))
                .filter(trade -> trade.getExchange().equals(destination))
                .filter(trade -> trade.getDateTime()
                    .isAfter(sepDate.plusSeconds(inc)))
                .collect(Collectors.toList());

            buyTrades.stream()
                .filter(trade -> !fiatToXrpTradeIds.contains(trade.getOrderId()))
                .collect(Collectors.groupingBy(Trade::getDateTime)).values().forEach(subBuyTrades -> {

                final long buyTimestamp = subBuyTrades.get(0).getTimestamp();
                final double amount = subBuyTrades.stream().mapToDouble(Trade::getAmount).sum();

                if (amount % 1 != 0 && amount > 500) {
                    sellTTrades.stream()
                        .filter(trade -> !xrpToFiatTradeIds.contains(trade.getOrderId()))
                        .filter(trade -> (trade.getTimestamp() - buyTimestamp) <= 30 * 1000)

                        .collect(Collectors.groupingBy(Trade::getDateTime)).values().forEach(subSellTrades -> {

                        final double sellAmount = subSellTrades.stream().mapToDouble(Trade::getAmount).sum();

                        if (amount - sellAmount <= 0.2) {
                            persistPayment(buildPayment(subBuyTrades, subSellTrades, amount, this.rate));
                        }
                    });
                }
            });
        }

        System.out.println("FIN");
    }

    private ExchangeToExchangePayment buildPayment(List<Trade> buy, List<Trade> sell, double amount, double rate) {
        String hash = "0FFCHAIN_" + sell.get(0).getTimestamp() + "_" + buy.get(0).getTimestamp();
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
            .tradeOutIds(sell.stream().map(Trade::getOrderId).collect(Collectors.joining(";")))
            .tradeIds(buy.stream().map(Trade::getOrderId).collect(Collectors.joining(";")))
            .fiatToXrpTradeIds(buy.stream().map(Trade::getOrderId).collect(Collectors.toList()))
            .xrpToFiatTradeIds(sell.stream().map(Trade::getOrderId).collect(Collectors.toList()))
            .build();
    }


    @Override
    public Exchange getDestinationExchange() {
        return null;
    }

    @Override
    public SpottedAt getSpottedAt() {
        return SpottedAt.OFF_CHAIN;
    }
}