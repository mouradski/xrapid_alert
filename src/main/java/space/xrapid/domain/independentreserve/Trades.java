package space.xrapid.domain.independentreserve;

import com.fasterxml.jackson.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "Trades",
        "PrimaryCurrencyCode",
        "SecondaryCurrencyCode",
        "CreatedTimestampUtc"
})
public class Trades {

    @JsonProperty("Trades")
    private List<Trade> trades = null;
    @JsonProperty("PrimaryCurrencyCode")
    private String primaryCurrencyCode;
    @JsonProperty("SecondaryCurrencyCode")
    private String secondaryCurrencyCode;
    @JsonProperty("CreatedTimestampUtc")
    private String createdTimestampUtc;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("Trades")
    public List<Trade> getTrades() {
        return trades;
    }

    @JsonProperty("Trades")
    public void setTrades(List<Trade> trades) {
        this.trades = trades;
    }

    @JsonProperty("PrimaryCurrencyCode")
    public String getPrimaryCurrencyCode() {
        return primaryCurrencyCode;
    }

    @JsonProperty("PrimaryCurrencyCode")
    public void setPrimaryCurrencyCode(String primaryCurrencyCode) {
        this.primaryCurrencyCode = primaryCurrencyCode;
    }

    @JsonProperty("SecondaryCurrencyCode")
    public String getSecondaryCurrencyCode() {
        return secondaryCurrencyCode;
    }

    @JsonProperty("SecondaryCurrencyCode")
    public void setSecondaryCurrencyCode(String secondaryCurrencyCode) {
        this.secondaryCurrencyCode = secondaryCurrencyCode;
    }

    @JsonProperty("CreatedTimestampUtc")
    public String getCreatedTimestampUtc() {
        return createdTimestampUtc;
    }

    @JsonProperty("CreatedTimestampUtc")
    public void setCreatedTimestampUtc(String createdTimestampUtc) {
        this.createdTimestampUtc = createdTimestampUtc;
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