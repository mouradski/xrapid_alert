package space.xrapid.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

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

  @JsonIgnore
  private OffsetDateTime dateTime;

  private Long timestamp;

  @Enumerated(EnumType.STRING)
  private Exchange source;

  private String sourceAddress;
  private String destinationAddress;

  @Enumerated(EnumType.STRING)
  private Exchange destination;
  private Double amount;

  @JsonIgnore
  private boolean confirmed;

  private SpottedAt spottedAt;

  @Column(unique = true)
  private String transactionHash;

  @Transient
  @JsonIgnore
  private List<Trade> xrpToFiatTrades;

  @Transient
  @JsonIgnore
  private List<String> xrpToFiatTradeIds;
  @Transient
  @JsonIgnore
  private List<Trade> fiatToXrpTrades;
  @Transient
  private List<String> fiatToXrpTradeIds;
  private double usdValue;
  @Column(length = 500)
  @JsonIgnore
  private String tradeIds;
  @Column(length = 500)
  @JsonIgnore
  private String tradeOutIds;
  @Enumerated(EnumType.STRING)
  private Currency sourceFiat;
  @Enumerated(EnumType.STRING)
  private Currency destinationFiat;
  private Long tag;
  @Enumerated(EnumType.STRING)
  @JsonIgnore
  private Currency destinationCurrencry;
  private boolean inTradeFound = false;
  private boolean outTradeFound = false;

  @JsonProperty("xrpToFiatTradeIds")
  public List<String> getXrpToFiatTradeIds() {
    if (tradeIds == null) {
      return null;
    }

    return Arrays.asList(tradeIds.split(";"));
  }

  @JsonProperty("fiatToXrpTradeIds")
  public List<String> getFiatToXrpTradeIds() {
    if (tradeOutIds == null) {
      return null;
    }

    return Arrays.asList(tradeOutIds.split(";"));
  }

  public String getDateAsString() {
    return timestamp == null ? null : dateFormat.format(timestamp);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("Time : ").append(dateFormat.format(timestamp)).append(", ");
    sb.append("Amount : ").append(this.amount).append(", ");
    sb.append("Exchange Source : ").append(this.source == null ? "UNKNOWN" : this.source)
        .append(", ");
    sb.append("Exchange Destination : ")
        .append(this.destination == null ? "UNKNOWN" : this.destination).append(", ");
    sb.append("Address Source : ").append(sourceAddress).append(", ");
    sb.append("Destination : ").append(this.destinationAddress).append(", ");
    sb.append("Destination Tag : ").append(this.tag).append(", ");
    sb.append("Trx Hash : ").append(this.transactionHash);

    return sb.toString();
  }

  public String toCsvLine() {

    return dateTime + ";" + timestamp + ";" + sourceAddress + ";" + source + ";"
        + destinationAddress + ";" + tag + ";" + destination + ";" + sourceFiat + ";"
        + destinationFiat + ";" + amount + ";" + usdValue + ";" + transactionHash + ";" + spottedAt;
  }
}
