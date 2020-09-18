package space.xrapid.domain.cexio;

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
    "type",
    "date",
    "amount",
    "price",
    "tid"
})
public class Trade {

  @JsonProperty("type")
  private String type;
  @JsonProperty("date")
  private long date;
  @JsonProperty("amount")
  private double amount;
  @JsonProperty("price")
  private double price;
  @JsonProperty("tid")
  private String tid;
  @JsonIgnore
  private Map<String, Object> additionalProperties = new HashMap<String, Object>();

  @JsonProperty("type")
  public String getType() {
    return type;
  }

  @JsonProperty("type")
  public void setType(String type) {
    this.type = type;
  }

  @JsonProperty("date")
  public long getDate() {
    return date;
  }

  @JsonProperty("date")
  public void setDate(long date) {
    this.date = date;
  }

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

  @JsonProperty("tid")
  public String getTid() {
    return tid;
  }

  @JsonProperty("tid")
  public void setTid(String tid) {
    this.tid = tid;
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