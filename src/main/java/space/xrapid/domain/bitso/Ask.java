package space.xrapid.domain.bitso;

import com.fasterxml.jackson.annotation.*;

import java.util.HashMap;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
"book",
"price",
"amount",
"oid"
})
public class Ask {

@JsonProperty("book")
private String book;
@JsonProperty("price")
private String price;
@JsonProperty("amount")
private String amount;
@JsonProperty("oid")
private String oid;
@JsonIgnore
private Map<String, Object> additionalProperties = new HashMap<String, Object>();

@JsonProperty("book")
public String getBook() {
return book;
}

@JsonProperty("book")
public void setBook(String book) {
this.book = book;
}

@JsonProperty("price")
public String getPrice() {
return price;
}

@JsonProperty("price")
public void setPrice(String price) {
this.price = price;
}

@JsonProperty("amount")
public String getAmount() {
return amount;
}

@JsonProperty("amount")
public void setAmount(String amount) {
this.amount = amount;
}

@JsonProperty("oid")
public String getOid() {
return oid;
}

@JsonProperty("oid")
public void setOid(String oid) {
this.oid = oid;
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
