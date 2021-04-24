package space.xrapid.domain.b2bx;

import com.fasterxml.jackson.annotation.*;

import javax.annotation.Generated;
import java.util.HashMap;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "tradeID",
        "price",
        "base_volume",
        "quote_volume",
        "trade_timestamp",
        "type"
})
@Generated("jsonschema2pojo")
public class Trade {

    @JsonProperty("tradeID")
    private String tradeID;
    @JsonProperty("price")
    private double price;
    @JsonProperty("base_volume")
    private double baseVolume;
    @JsonProperty("quote_volume")
    private double quoteVolume;
    @JsonProperty("trade_timestamp")
    private long tradeTimestamp;
    @JsonProperty("type")
    private String type;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("tradeID")
    public String getTradeID() {
        return tradeID;
    }

    @JsonProperty("tradeID")
    public void setTradeID(String tradeID) {
        this.tradeID = tradeID;
    }

    @JsonProperty("price")
    public double getPrice() {
        return price;
    }

    @JsonProperty("price")
    public void setPrice(double price) {
        this.price = price;
    }

    @JsonProperty("base_volume")
    public double getBaseVolume() {
        return baseVolume;
    }

    @JsonProperty("base_volume")
    public void setBaseVolume(double baseVolume) {
        this.baseVolume = baseVolume;
    }

    @JsonProperty("quote_volume")
    public double getQuoteVolume() {
        return quoteVolume;
    }

    @JsonProperty("quote_volume")
    public void setQuoteVolume(double quoteVolume) {
        this.quoteVolume = quoteVolume;
    }

    @JsonProperty("trade_timestamp")
    public Long getTradeTimestamp() {
        return tradeTimestamp;
    }

    @JsonProperty("trade_timestamp")
    public void setTradeTimestamp(Long tradeTimestamp) {
        this.tradeTimestamp = tradeTimestamp;
    }

    @JsonProperty("type")
    public String getType() {
        return type;
    }

    @JsonProperty("type")
    public void setType(String type) {
        this.type = type;
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