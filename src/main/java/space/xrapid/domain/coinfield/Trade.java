package space.xrapid.domain.coinfield;

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
    "volume",
    "total_value",
    "timestamp"
})
public class Trade {

  @JsonProperty("id")
  private String id;
  @JsonProperty("price")
  private double price;
  @JsonProperty("volume")
  private double volume;
  @JsonProperty("total_value")
  private double totalValue;
  @JsonProperty("timestamp")
  private String timestamp;
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

  @JsonProperty("volume")
  public double getVolume() {
    return volume;
  }

  @JsonProperty("volume")
  public void setVolume(double volume) {
    this.volume = volume;
  }

  @JsonProperty("total_value")
  public double getTotalValue() {
    return totalValue;
  }

  @JsonProperty("total_value")
  public void setTotalValue(double totalValue) {
    this.totalValue = totalValue;
  }

  @JsonProperty("timestamp")
  public String getTimestamp() {
    return timestamp;
  }

  @JsonProperty("timestamp")
  public void setTimestamp(String timestamp) {
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
