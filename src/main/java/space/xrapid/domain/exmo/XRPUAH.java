package space.xrapid.domain.exmo;

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
    "trade_id",
    "type",
    "quantity",
    "price",
    "amount",
    "date"
})
public class XRPUAH {

  @JsonProperty("trade_id")
  private String tradeId;
  @JsonProperty("type")
  private String type;
  @JsonProperty("quantity")
  private Double quantity;
  @JsonProperty("price")
  private Double price;
  @JsonProperty("amount")
  private Double amount;
  @JsonProperty("date")
  private Long date;
  @JsonIgnore
  private Map<String, Object> additionalProperties = new HashMap<String, Object>();

  @JsonProperty("trade_id")
  public String getTradeId() {
    return tradeId;
  }

  @JsonProperty("trade_id")
  public void setTradeId(String tradeId) {
    this.tradeId = tradeId;
  }

  @JsonProperty("type")
  public String getType() {
    return type;
  }

  @JsonProperty("type")
  public void setType(String type) {
    this.type = type;
  }

  @JsonProperty("quantity")
  public Double getQuantity() {
    return quantity;
  }

  @JsonProperty("quantity")
  public void setQuantity(Double quantity) {
    this.quantity = quantity;
  }

  @JsonProperty("price")
  public Double getPrice() {
    return price;
  }

  @JsonProperty("price")
  public void setPrice(Double price) {
    this.price = price;
  }

  @JsonProperty("amount")
  public Double getAmount() {
    return amount;
  }

  @JsonProperty("amount")
  public void setAmount(Double amount) {
    this.amount = amount;
  }

  @JsonProperty("date")
  public Long getDate() {
    return date;
  }

  @JsonProperty("date")
  public void setDate(Long date) {
    this.date = date;
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
