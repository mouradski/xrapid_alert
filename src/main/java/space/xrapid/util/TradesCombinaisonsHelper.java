package space.xrapid.util;

import org.paukov.combinatorics3.Generator;
import org.paukov.combinatorics3.IGenerator;
import space.xrapid.domain.Trade;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class TradesCombinaisonsHelper {


    public static List<Trade> getTrades(List<Trade> trades, double amount, boolean buy, double delta) {

        int maxSize = trades.size() > 22 ? 22 : trades.size();

        for (int i = 1; i <= maxSize; i++) {

            long start = new Date().getTime();

            IGenerator<List<Trade>> combinaisons = Generator.combination(trades).simple(i);

            Iterator<List<Trade>> iterator = combinaisons.iterator();

            if (new Date().getTime() - start > 30000) {
                break;
            }

            while (iterator.hasNext()) {
                List<Trade> candidates = iterator.next();

                double sum = sum(candidates);
                double diff = buy ? sum - amount : amount - sum;

                if (diff >= 0 && diff <= 0.5) {
                    return candidates;
                }

            }
        }

        return new ArrayList<>();
    }


    public static Double sum(List<Trade> groups) {
        return groups.stream().mapToDouble(Trade::getAmount).sum();
    }

}
