package space.xrapid.domain.btcmarkets;

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
    "id",
    "price",
    "amount",
    "timestamp",
    "side"
})
public class Trade {

  @JsonProperty("id")
  private String id;
  @JsonProperty("price")
  private double price;
  @JsonProperty("amount")
  private double amount;
  @JsonProperty("timestamp")
  private String timestamp;
  @JsonProperty("side")
  private String side;
  @JsonIgnore
  private Map<String, Object> additionalProperties = new HashMap<String, Object>();

  @JsonProperty("id")
  public String getId() {
    return id;
  }

  @JsonProperty("id")
  public void setId(String id) {
    this.id = id;
  }

  @JsonProperty("price")
  public double getPrice() {
    return price;
  }

  @JsonProperty("price")
  public void setPrice(double price) {
    this.price = price;
  }

  @JsonProperty("amount")
  public double getAmount() {
    return amount;
  }

  @JsonProperty("amount")
  public void setAmount(double amount) {
    this.amount = amount;
  }

  @JsonProperty("timestamp")
  public String getTimestamp() {
    return timestamp;
  }

  @JsonProperty("timestamp")
  public void setTimestamp(String timestamp) {
    this.timestamp = timestamp;
  }

  @JsonProperty("side")
  public String getSide() {
    return side;
  }

  @JsonProperty("side")
  public void setSide(String side) {
    this.side = side;
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