package space.xrapid.domain.coinone;

import com.fasterxml.jackson.annotation.*;

import java.util.HashMap;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "is_ask",
    "timestamp",
    "price",
    "id",
    "qty"
})
public class CompleteOrder {

    @JsonProperty("is_ask")
    private String isAsk;
    @JsonProperty("timestamp")
    private Long timestamp;
    @JsonProperty("price")
    private double price;
    @JsonProperty("id")
    private String id;
    @JsonProperty("qty")
    private double qty;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("is_ask")
    public String getIsAsk() {
        return isAsk;
    }

    @JsonProperty("is_ask")
    public void setIsAsk(String isAsk) {
        this.isAsk = isAsk;
    }

    @JsonProperty("timestamp")
    public Long getTimestamp() {
        return timestamp;
    }

    @JsonProperty("timestamp")
    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    @JsonProperty("price")
    public double getPrice() {
        return price;
    }

    @JsonProperty("price")
    public void setPrice(double price) {
        this.price = price;
    }

    @JsonProperty("id")
    public String getId() {
        return id;
    }

    @JsonProperty("id")
    public void setId(String id) {
        this.id = id;
    }

    @JsonProperty("qty")
    public double getQty() {
        return qty;
    }

    @JsonProperty("qty")
    public void setQty(double qty) {
        this.qty = qty;
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
