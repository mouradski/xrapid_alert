package space.xrapid.domain.novadax;

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
    "amount",
    "price",
    "side",
    "timestamp"
})
public class Datum {

  @JsonProperty("amount")
  private double amount;
  @JsonProperty("price")
  private double price;
  @JsonProperty("side")
  private String side;
  @JsonProperty("timestamp")
  private long timestamp;
  @JsonIgnore
  private Map<String, Object> additionalProperties = new HashMap<String, Object>();

  @JsonProperty("amount")
  public double getAmount() {
    return amount;
  }

  @JsonProperty("amount")
  public void setAmount(double amount) {
    this.amount = amount;
  }

  @JsonProperty("price")
  public double getPrice() {
    return price;
  }

  @JsonProperty("price")
  public void setPrice(double price) {
    this.price = price;
  }

  @JsonProperty("side")
  public String getSide() {
    return side;
  }

  @JsonProperty("side")
  public void setSide(String side) {
    this.side = side;
  }

  @JsonProperty("timestamp")
  public long getTimestamp() {
    return timestamp;
  }

  @JsonProperty("timestamp")
  public void setTimestamp(long timestamp) {
    this.timestamp = timestamp;
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