package space.xrapid.domain.bitcointrade;

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
    "amount",
    "unit_price",
    "active_order_code",
    "passive_order_code",
    "date"
})
public class Trade {

  @JsonProperty("type")
  private String type;
  @JsonProperty("amount")
  private Double amount;
  @JsonProperty("unit_price")
  private Double unitPrice;
  @JsonProperty("active_order_code")
  private String activeOrderCode;
  @JsonProperty("passive_order_code")
  private String passiveOrderCode;
  @JsonProperty("date")
  private String date;
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

  @JsonProperty("amount")
  public Double getAmount() {
    return amount;
  }

  @JsonProperty("amount")
  public void setAmount(Double amount) {
    this.amount = amount;
  }

  @JsonProperty("unit_price")
  public Double getUnitPrice() {
    return unitPrice;
  }

  @JsonProperty("unit_price")
  public void setUnitPrice(Double unitPrice) {
    this.unitPrice = unitPrice;
  }

  @JsonProperty("active_order_code")
  public String getActiveOrderCode() {
    return activeOrderCode;
  }

  @JsonProperty("active_order_code")
  public void setActiveOrderCode(String activeOrderCode) {
    this.activeOrderCode = activeOrderCode;
  }

  @JsonProperty("passive_order_code")
  public String getPassiveOrderCode() {
    return passiveOrderCode;
  }

  @JsonProperty("passive_order_code")
  public void setPassiveOrderCode(String passiveOrderCode) {
    this.passiveOrderCode = passiveOrderCode;
  }

  @JsonProperty("date")
  public String getDate() {
    return date;
  }

  @JsonProperty("date")
  public void setDate(String date) {
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