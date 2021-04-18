package space.xrapid.domain.liquid;

import com.fasterxml.jackson.annotation.*;

import java.util.HashMap;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "id",
        "quantity",
        "price",
        "taker_side",
        "created_at"
})
public class Trade {

    @JsonProperty("id")
    private String id;
    @JsonProperty("quantity")
    private double quantity;
    @JsonProperty("price")
    private double price;
    @JsonProperty("taker_side")
    private String takerSide;
    @JsonProperty("created_at")
    private Long createdAt;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("id")
    public String getId() {
        return id;
    }

    @JsonProperty("id")
    public void setId(String id) {
        this.id = id;
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

    @JsonProperty("taker_side")
    public String getTakerSide() {
        return takerSide;
    }

    @JsonProperty("taker_side")
    public void setTakerSide(String takerSide) {
        this.takerSide = takerSide;
    }

    @JsonProperty("created_at")
    public Long getCreatedAt() {
        return createdAt;
    }

    @JsonProperty("created_at")
    public void setCreatedAt(Long createdAt) {
        this.createdAt = createdAt;
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