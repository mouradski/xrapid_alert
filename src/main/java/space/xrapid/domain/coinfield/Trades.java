package space.xrapid.domain.coinfield;

import com.fasterxml.jackson.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "market",
        "trades_hash",
        "trades",
        "timestamp",
        "took"
})
public class Trades {

    @JsonProperty("market")
    private String market;
    @JsonProperty("trades_hash")
    private String tradesHash;
    @JsonProperty("trades")
    private List<Trade> trades = null;
    @JsonProperty("timestamp")
    private String timestamp;
    @JsonProperty("took")
    private String took;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("market")
    public String getMarket() {
        return market;
    }

    @JsonProperty("market")
    public void setMarket(String market) {
        this.market = market;
    }

    @JsonProperty("trades_hash")
    public String getTradesHash() {
        return tradesHash;
    }

    @JsonProperty("trades_hash")
    public void setTradesHash(String tradesHash) {
        this.tradesHash = tradesHash;
    }

    @JsonProperty("trades")
    public List<Trade> getTrades() {
        return trades;
    }

    @JsonProperty("trades")
    public void setTrades(List<Trade> trades) {
        this.trades = trades;
    }

    @JsonProperty("timestamp")
    public String getTimestamp() {
        return timestamp;
    }

    @JsonProperty("timestamp")
    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    @JsonProperty("took")
    public String getTook() {
        return took;
    }

    @JsonProperty("took")
    public void setTook(String took) {
        this.took = took;
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
