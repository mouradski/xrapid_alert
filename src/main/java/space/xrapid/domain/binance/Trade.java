package space.xrapid.domain.binance;

import java.util.HashMap;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
"id",
"price",
"qty",
"quoteQty",
"time",
"isBuyerMaker",
"isBestMatch"
})
public class Trade {

@JsonProperty("id")
private Integer id;
@JsonProperty("price")
private Double price;
@JsonProperty("qty")
private Double qty;
@JsonProperty("quoteQty")
private Double quoteQty;
@JsonProperty("time")
private Long time;
@JsonProperty("isBuyerMaker")
private Boolean isBuyerMaker;
@JsonProperty("isBestMatch")
private Boolean isBestMatch;
@JsonIgnore
private Map<String, Object> additionalProperties = new HashMap<String, Object>();

@JsonProperty("id")
public Integer getId() {
return id;
}

@JsonProperty("id")
public void setId(Integer id) {
this.id = id;
}

@JsonProperty("price")
public Double getPrice() {
return price;
}

@JsonProperty("price")
public void setPrice(Double price) {
this.price = price;
}

@JsonProperty("qty")
public Double getQty() {
return qty;
}

@JsonProperty("qty")
public void setQty(Double qty) {
this.qty = qty;
}

@JsonProperty("quoteQty")
public Double getQuoteQty() {
return quoteQty;
}

@JsonProperty("quoteQty")
public void setQuoteQty(Double quoteQty) {
this.quoteQty = quoteQty;
}

@JsonProperty("time")
public Long getTime() {
return time;
}

@JsonProperty("time")
public void setTime(Long time) {
this.time = time;
}

@JsonProperty("isBuyerMaker")
public Boolean getIsBuyerMaker() {
return isBuyerMaker;
}

@JsonProperty("isBuyerMaker")
public void setIsBuyerMaker(Boolean isBuyerMaker) {
this.isBuyerMaker = isBuyerMaker;
}

@JsonProperty("isBestMatch")
public Boolean getIsBestMatch() {
return isBestMatch;
}

@JsonProperty("isBestMatch")
public void setIsBestMatch(Boolean isBestMatch) {
this.isBestMatch = isBestMatch;
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