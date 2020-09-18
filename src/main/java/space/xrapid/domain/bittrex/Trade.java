package space.xrapid.domain.bittrex;

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
    "Id",
    "TimeStamp",
    "Quantity",
    "Price",
    "Total",
    "FillType",
    "OrderType",
    "Uuid"
})
public class Trade {

  @JsonProperty("Id")
  private Integer id;
  @JsonProperty("TimeStamp")
  private String timeStamp;
  @JsonProperty("Quantity")
  private Double quantity;
  @JsonProperty("Price")
  private Double price;
  @JsonProperty("Total")
  private Double total;
  @JsonProperty("FillType")
  private String fillType;
  @JsonProperty("OrderType")
  private String orderType;
  @JsonProperty("Uuid")
  private String uuid;
  @JsonIgnore
  private Map<String, Object> additionalProperties = new HashMap<String, Object>();

  @JsonProperty("Id")
  public Integer getId() {
    return id;
  }

  @JsonProperty("Id")
  public void setId(Integer id) {
    this.id = id;
  }

  @JsonProperty("TimeStamp")
  public String getTimeStamp() {
    return timeStamp;
  }

  @JsonProperty("TimeStamp")
  public void setTimeStamp(String timeStamp) {
    this.timeStamp = timeStamp;
  }

  @JsonProperty("Quantity")
  public Double getQuantity() {
    return quantity;
  }

  @JsonProperty("Quantity")
  public void setQuantity(Double quantity) {
    this.quantity = quantity;
  }

  @JsonProperty("Price")
  public Double getPrice() {
    return price;
  }

  @JsonProperty("Price")
  public void setPrice(Double price) {
    this.price = price;
  }

  @JsonProperty("Total")
  public Double getTotal() {
    return total;
  }

  @JsonProperty("Total")
  public void setTotal(Double total) {
    this.total = total;
  }

  @JsonProperty("FillType")
  public String getFillType() {
    return fillType;
  }

  @JsonProperty("FillType")
  public void setFillType(String fillType) {
    this.fillType = fillType;
  }

  @JsonProperty("OrderType")
  public String getOrderType() {
    return orderType;
  }

  @JsonProperty("OrderType")
  public void setOrderType(String orderType) {
    this.orderType = orderType;
  }

  @JsonProperty("Uuid")
  public String getUuid() {
    return uuid;
  }

  @JsonProperty("Uuid")
  public void setUuid(String uuid) {
    this.uuid = uuid;
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