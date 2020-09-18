package space.xrapid.domain.upbit;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.util.HashMap;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "market",
    "trade_date_utc",
    "trade_time_utc",
    "timestamp",
    "trade_price",
    "trade_volume",
    "prev_closing_price",
    "change_price",
    "ask_bid",
    "sequential_id"
})
public class Trade {

  @JsonProperty("timestamp")
  private Long timestamp;
  @JsonProperty("trade_price")
  private Double tradePrice;
  @JsonProperty("trade_volume")
  private Double tradeVolume;
  @JsonProperty("ask_bid")
  private String askBid;
  @JsonProperty("sequential_id")
  private String sequentialId;
  @JsonIgnore
  private Map<String, Object> additionalProperties = new HashMap<String, Object>();

  @JsonProperty("timestamp")
  public Long getTimestamp() {
    return timestamp;
  }

  @JsonProperty("timestamp")
  public void setTimestamp(Long timestamp) {
    this.timestamp = timestamp;
  }

  @JsonProperty("trade_price")
  public Double getTradePrice() {
    return tradePrice;
  }

  @JsonProperty("trade_price")
  public void setTradePrice(Double tradePrice) {
    this.tradePrice = tradePrice;
  }

  @JsonProperty("trade_volume")
  public Double getTradeVolume() {
    return tradeVolume;
  }

  @JsonProperty("trade_volume")
  public void setTradeVolume(Double tradeVolume) {
    this.tradeVolume = tradeVolume;
  }

  @JsonProperty("ask_bid")
  public String getAskBid() {
    return askBid;
  }

  @JsonProperty("ask_bid")
  public void setAskBid(String askBid) {
    this.askBid = askBid;
  }

  @JsonProperty("sequential_id")
  public String getSequentialId() {
    return sequentialId;
  }

  @JsonProperty("sequential_id")
  public void setSequentialId(String sequentialId) {
    this.sequentialId = sequentialId;
  }

  @JsonAnyGetter
  public Map<String, Object> getAdditionalProperties() {
    return this.additionalProperties;
  }

  @JsonAnySetter
  public void setAdditionalProperty(String name, Object value) {
    this.additionalProperties.put(name, value);
  }

}
