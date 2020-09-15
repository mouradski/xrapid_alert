package space.xrapid.domain.luno;

import com.fasterxml.jackson.annotation.*;

import java.util.HashMap;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "sequence",
        "timestamp",
        "price",
        "volume",
        "is_buy"
})
public class Trade {

    @JsonProperty("sequence")
    private Long sequence;
    @JsonProperty("timestamp")
    private Long timestamp;
    @JsonProperty("price")
    private Double price;
    @JsonProperty("volume")
    private Double volume;
    @JsonProperty("is_buy")
    private Boolean isBuy;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("sequence")
    public Long getSequence() {
        return sequence;
    }

    @JsonProperty("sequence")
    public void setSequence(Long sequence) {
        this.sequence = sequence;
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
    public Double getPrice() {
        return price;
    }

    @JsonProperty("price")
    public void setPrice(Double price) {
        this.price = price;
    }

    @JsonProperty("volume")
    public Double getVolume() {
        return volume;
    }

    @JsonProperty("volume")
    public void setVolume(Double volume) {
        this.volume = volume;
    }

    @JsonProperty("is_buy")
    public Boolean getIsBuy() {
        return isBuy;
    }

    @JsonProperty("is_buy")
    public void setIsBuy(Boolean isBuy) {
        this.isBuy = isBuy;
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