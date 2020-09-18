package space.xrapid.util;

import java.util.List;
import space.xrapid.domain.ExchangeToExchangePayment;

public class CsvHelper {

  public static String toCsv(List<ExchangeToExchangePayment> payments) {
    String headers = "dateTime;timestamp;sourceAddress;source;destinationAddress;tag;destination;sourceFiat;destinationFiat;amount;usdValue;transactionHash;spottedAt";

    StringBuilder sb = new StringBuilder(headers).append("\n");

    payments.stream().map(ExchangeToExchangePayment::toCsvLine).forEach(payment -> {
      sb.append(payment).append("\n");
    });

    return sb.toString();
  }
}
