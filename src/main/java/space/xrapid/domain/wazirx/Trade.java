package space.xrapid.domain.wazirx;

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
"volume",
"funds",
"market",
"created_at",
"side"
})
public class Trade {

@JsonProperty("id")
private Integer id;
@JsonProperty("price")
private double price;
@JsonProperty("volume")
private double volume;
@JsonProperty("funds")
private double funds;
@JsonProperty("market")
private String market;
@JsonProperty("created_at")
private String createdAt;
@JsonProperty("side")
private String side;
@JsonIgnore
private Map<String, Object> additionalProperties = new HashMap<>();

@JsonProperty("id")
public Integer getId() {
return id;
}

@JsonProperty("id")
public void setId(Integer id) {
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

@JsonProperty("funds")
public double getFunds() {
return funds;
}

@JsonProperty("funds")
public void setFunds(double funds) {
this.funds = funds;
}

@JsonProperty("market")
public String getMarket() {
return market;
}

@JsonProperty("market")
public void setMarket(String market) {
this.market = market;
}

@JsonProperty("created_at")
public String getCreatedAt() {
return createdAt;
}

@JsonProperty("created_at")
public void setCreatedAt(String createdAt) {
this.createdAt = createdAt;
}

@JsonProperty("side")
public String getSide() {
return side;
}

@JsonProperty("side")
public void setSide(String side) {
this.side = side;
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