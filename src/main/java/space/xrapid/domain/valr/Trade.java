package space.xrapid.domain.valr;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
"price",
"quantity",
"currencyPair",
"tradedAt",
"takerSide",
"sequenceId",
"id",
"quoteVolume"
})
public class Trade {

@JsonProperty("price")
private String price;
@JsonProperty("quantity")
private String quantity;
@JsonProperty("currencyPair")
private String currencyPair;
@JsonProperty("tradedAt")
private String tradedAt;
@JsonProperty("takerSide")
private String takerSide;
@JsonProperty("sequenceId")
private Integer sequenceId;
@JsonProperty("id")
private String id;
@JsonProperty("quoteVolume")
private String quoteVolume;
@JsonIgnore
private Map<String, Object> additionalProperties = new HashMap<String, Object>();

@JsonProperty("price")
public String getPrice() {
return price;
}

@JsonProperty("price")
public void setPrice(String price) {
this.price = price;
}

@JsonProperty("quantity")
public String getQuantity() {
return quantity;
}

@JsonProperty("quantity")
public void setQuantity(String quantity) {
this.quantity = quantity;
}

@JsonProperty("currencyPair")
public String getCurrencyPair() {
return currencyPair;
}

@JsonProperty("currencyPair")
public void setCurrencyPair(String currencyPair) {
this.currencyPair = currencyPair;
}

@JsonProperty("tradedAt")
public String getTradedAt() {
return tradedAt;
}

@JsonProperty("tradedAt")
public void setTradedAt(String tradedAt) {
this.tradedAt = tradedAt;
}

@JsonProperty("takerSide")
public String getTakerSide() {
return takerSide;
}

@JsonProperty("takerSide")
public void setTakerSide(String takerSide) {
this.takerSide = takerSide;
}

@JsonProperty("sequenceId")
public Integer getSequenceId() {
return sequenceId;
}

@JsonProperty("sequenceId")
public void setSequenceId(Integer sequenceId) {
this.sequenceId = sequenceId;
}

@JsonProperty("id")
public String getId() {
return id;
}

@JsonProperty("id")
public void setId(String id) {
this.id = id;
}

@JsonProperty("quoteVolume")
public String getQuoteVolume() {
return quoteVolume;
}

@JsonProperty("quoteVolume")
public void setQuoteVolume(String quoteVolume) {
this.quoteVolume = quoteVolume;
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