package space.xrapid.domain;

import lombok.*;

import javax.persistence.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.OffsetDateTime;
import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Data
@Entity(name = "EXCHANGE_PAYMENT")
public class ExchangeToExchangePayment extends Payment {

    private static DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss'Z'");

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private OffsetDateTime dateTime;
    private Long timestamp;

    @Enumerated(EnumType.STRING)
    private Exchange source;

    private String sourceAddress;
    private String destinationAddress;

    @Enumerated(EnumType.STRING)
    private Exchange destination;
    private Double amount;
    private boolean confirmed;
    private SpottedAt spottedAt;

    @Column(unique=true)
    private String transactionHash;

    @Transient
    private List<Trade> toFiatTrades;

    private double usdValue;

    @Column(length = 386)
    private String tradeIds;

    private Long tag;

    @Enumerated(EnumType.STRING)
    private Currency destinationCurrencry;

    public String getDateAsString() {
        return dateFormat.format(timestamp);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Time : ").append(dateFormat.format(timestamp)).append(", ");
        sb.append("Amount : ").append(this.amount).append(", ");
        sb.append("Exchange Source : ").append(this.source == null ? "UNKNOWN" : this.source).append(", ");
        sb.append("Address Source : ").append(sourceAddress).append(", ");
        sb.append("Destination : ").append(this.destinationAddress).append(", ");
        sb.append("Destination Tag : ").append(this.tag).append(", ");
        sb.append("Trx Hash : ").append(this.transactionHash);

        return sb.toString();
    }
}
