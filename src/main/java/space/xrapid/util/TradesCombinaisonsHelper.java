package space.xrapid.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.commons.lang3.tuple.Pair;
import space.xrapid.domain.Exchange;
import space.xrapid.domain.Trade;

public class TradesCombinaisonsHelper {

  public static List<Trade> getTrades(List<Trade> trades, double amount, String side) {
    List<Trade> toReturn = new ArrayList<>();

    double min = 10000;

    List<Pair<Double, List<Trade>>> tradesByTimestamp = trades.stream()
        .collect(Collectors.groupingBy(Trade::getTimestamp)).values().stream()
        .map(TradesCombinaisonsHelper::sums).collect(Collectors.toList());

    for (Pair<Double, List<Trade>> pair : tradesByTimestamp) {
      double sum = pair.getLeft();
      double diff = calculateDiff(amount, sum, side);

      if ((diff >= 0 && (diff <= 0.04005 || bitstampe(trades, sum, diff))) || bitso(trades, sum,
          diff)) {

        if (diff <= 0.02) {
          return pair.getRight();
        }

        if (diff < min) {
          toReturn = pair.getRight();
          min = diff;
        }
      }
    }

    return toReturn;
  }


  private static Pair<Double, List<Trade>> sums(List<Trade> trades) {
    return Pair.of(sum(trades), trades);
  }

  private static boolean bitstampe(List<Trade> trades, double sum, double diff) {
    if (!Arrays.asList(Exchange.BITSTAMP, Exchange.BITSTAMP_GBP, Exchange.BITSTAMP_EUR)
        .contains(trades.get(0).getExchange()) || sum < 10000) {
      return false;
    }

    return diff <= sum * 0.05;
  }

  private static boolean bitso(List<Trade> trades, double sum, double diff) {
    if (!trades.get(0).getExchange().equals(Exchange.BITSO) || sum < 10000) {
      return false;
    }

    return Math.abs(diff) <= sum * 0.02;
  }

  private static double calculateDiff(double amount, double sum, String side) {
    return "buy".equals(side) ? sum - amount : amount - sum;
  }

  public static Double sum(List<Trade> groups) {
    return groups.stream().mapToDouble(Trade::getAmount).sum();
  }

}
