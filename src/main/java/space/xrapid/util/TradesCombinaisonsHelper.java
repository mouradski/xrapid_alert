package space.xrapid.util;

import org.paukov.combinatorics3.Generator;
import org.paukov.combinatorics3.IGenerator;
import space.xrapid.domain.Trade;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class TradesCombinaisonsHelper {

    public static List<Trade> getTrades(List<Trade> trades, double amount, String side) {
        int maxSize = Math.min(trades.size(), 16);

        if (trades.size() > 20) {
            List<List<Trade>> candidates = new ArrayList<>();

            sub(trades, 20).parallelStream().forEach(tradeGroup -> {
                List<Trade> groupCandidate = getTrades(tradeGroup, amount, Math.min(tradeGroup.size(), 16), side);
                if (!groupCandidate.isEmpty()) {
                    candidates.add(groupCandidate);
                }
            });

            if (!candidates.isEmpty()) {
                return getClosestGroup(candidates, amount);
            } else {
                return new ArrayList<>();
            }
        }


        return getTrades(trades, amount, maxSize, side);
    }

    private static List<Trade> getTrades(List<Trade> trades, double amount, int maxSize, String side) {

        List<Trade> toReturn = new ArrayList<>();

        double min = 10000;

        for (int i = 1; i <= maxSize; i++) {

            long start = new Date().getTime();

            IGenerator<List<Trade>> combinaisons = Generator.combination(trades).simple(i);

            Iterator<List<Trade>> iterator = combinaisons.iterator();

            while (iterator.hasNext()) {
                List<Trade> candidates = iterator.next();

                double sum = sum(candidates);
                double diff = calculateDiff(amount, sum, side);

                if (diff <= 0.04005) {

                    if (diff <= 0.02) {
                        return candidates;
                    }

                    if (diff < min) {
                        toReturn = candidates;
                        min = diff;

                        if (new Date().getTime() - start > 15000) {
                            break;
                        }
                    }
                }
            }
        }

        return toReturn;
    }

    public static List<List<Trade>> sub(List<Trade> trades, int size) {

        List<List<Trade>> result = new ArrayList<>();

        int index = 0;

        while (index < trades.size()) {
            if ((index + size) <= trades.size()) {
                result.add(trades.subList(index, index + size));
            } else {
                result.add(trades.subList(index, trades.size()));
                break;
            }
            index += 10;
        }

        return result;
    }

    private static double calculateDiff(double amount, double sum, String side) {
        double diff = "buy".equals(side) ? sum - amount : amount - sum;
        return diff >= 0 ? diff : Double.POSITIVE_INFINITY;
    }

    private static List<Trade> getClosestGroup(List<List<Trade>> trades, double amount) {
        double minDiff = 5000;
        List<Trade> result = new ArrayList<>();
        for (List<Trade> tradeGroup : trades) {
            double sum = sum(tradeGroup);
            double diff = Math.abs(sum - amount);
            if (diff < minDiff) {
                minDiff = diff;
                result = tradeGroup;
            }
        }

        return result;
    }

    public static Double sum(List<Trade> groups) {
        return groups.stream().mapToDouble(Trade::getAmount).sum();
    }

}
