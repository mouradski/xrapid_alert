package space.xrapid.domain.bitstamp;

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
"date",
"tid",
"price",
"type",
"amount"
})
public class Trade {

@JsonProperty("date")
private Long date;
@JsonProperty("tid")
private String tid;
@JsonProperty("price")
private String price;
@JsonProperty("type")
private String type;
@JsonProperty("amount")
private String amount;
@JsonIgnore
private Map<String, Object> additionalProperties = new HashMap<String, Object>();

@JsonProperty("date")
public Long getDate() {
return date;
}

@JsonProperty("date")
public void setDate(Long date) {
this.date = date;
}

@JsonProperty("tid")
public String getTid() {
return tid;
}

@JsonProperty("tid")
public void setTid(String tid) {
this.tid = tid;
}

@JsonProperty("price")
public String getPrice() {
return price;
}

@JsonProperty("price")
public void setPrice(String price) {
this.price = price;
}

@JsonProperty("type")
public String getType() {
return type;
}

@JsonProperty("type")
public void setType(String type) {
this.type = type;
}

@JsonProperty("amount")
public String getAmount() {
return amount;
}

@JsonProperty("amount")
public void setAmount(String amount) {
this.amount = amount;
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
