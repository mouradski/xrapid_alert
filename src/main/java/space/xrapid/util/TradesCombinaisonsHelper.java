package space.xrapid.util;

import org.paukov.combinatorics3.Generator;
import space.xrapid.domain.Trade;

import java.util.ArrayList;
import java.util.List;

public class TradesCombinaisonsHelper {

    public static List<List<Trade>> combinaisons(List<Trade> trades, double amount, boolean buy) {

        List<List<Trade>> combinaisons = new ArrayList<>();

        boolean found = false;
        for (int i = 1; i <= 30; i++) {
            Generator.combination(trades)
                    .simple(i).stream().forEach(g -> {

                combinaisons.add(g);
            });

            for (List<Trade> list : combinaisons) {
                if ((buy && Math.abs(sum(list) - amount) <= amount * 0.0001) || (!buy && (amount - sum(list)) <= amount * 0.0001)) {
                    found = true;
                    break;
                }
            }

            if (found) {
                break;
            }

        }

        return combinaisons;
    }

    public static List<Trade> closestGroup(List<Trade> trades, double amount, boolean buy) {

        return combinaisons(trades, amount, buy).stream()
                .sorted((g1, g2) -> Double.valueOf(Math.abs(sum(g1) - amount)).compareTo(Math.abs(sum(g2) - amount))).findFirst().get();
    }

    public static Double sum(List<Trade> groups) {
        return groups.stream().mapToDouble(Trade::getAmount).sum();
    }

}
