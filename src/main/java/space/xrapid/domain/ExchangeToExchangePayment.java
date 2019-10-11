package space.xrapid.domain;

import lombok.*;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Data
public class ExchangeToExchangePayment extends Payment {

    private static DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss'Z'");

    private Long timestamp;
    private Exchange source;
    private Exchange destination;
    private Double amount;
    private String transactionHash;

    public String getDateAsString() {
        return dateFormat.format(timestamp);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Time : ").append(dateFormat.format(timestamp)).append(", ");
        sb.append("Amount : ").append(this.amount).append(", ");
        sb.append("Source : ").append(this.source).append(", ");
        sb.append("Destination : ").append(this.destination);
        return sb.toString();
    }
}
