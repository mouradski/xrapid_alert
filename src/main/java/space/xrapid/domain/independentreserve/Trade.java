package space.xrapid.domain.independentreserve;

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
    "TradeTimestampUtc",
    "PrimaryCurrencyAmount",
    "SecondaryCurrencyTradePrice"
})
public class Trade {

  @JsonProperty("TradeTimestampUtc")
  private String tradeTimestampUtc;
  @JsonProperty("PrimaryCurrencyAmount")
  private Double primaryCurrencyAmount;
  @JsonProperty("SecondaryCurrencyTradePrice")
  private Double secondaryCurrencyTradePrice;
  @JsonIgnore
  private Map<String, Object> additionalProperties = new HashMap<String, Object>();

  @JsonProperty("TradeTimestampUtc")
  public String getTradeTimestampUtc() {
    return tradeTimestampUtc;
  }

  @JsonProperty("TradeTimestampUtc")
  public void setTradeTimestampUtc(String tradeTimestampUtc) {
    this.tradeTimestampUtc = tradeTimestampUtc;
  }

  @JsonProperty("PrimaryCurrencyAmount")
  public Double getPrimaryCurrencyAmount() {
    return primaryCurrencyAmount;
  }

  @JsonProperty("PrimaryCurrencyAmount")
  public void setPrimaryCurrencyAmount(Double primaryCurrencyAmount) {
    this.primaryCurrencyAmount = primaryCurrencyAmount;
  }

  @JsonProperty("SecondaryCurrencyTradePrice")
  public Double getSecondaryCurrencyTradePrice() {
    return secondaryCurrencyTradePrice;
  }

  @JsonProperty("SecondaryCurrencyTradePrice")
  public void setSecondaryCurrencyTradePrice(Double secondaryCurrencyTradePrice) {
    this.secondaryCurrencyTradePrice = secondaryCurrencyTradePrice;
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