package space.xrapid.domain.bitcointrade;

import com.fasterxml.jackson.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "pagination",
        "trades"
})
public class Data {

    @JsonProperty("pagination")
    private Pagination pagination;
    @JsonProperty("trades")
    private List<Trade> trades = null;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("pagination")
    public Pagination getPagination() {
        return pagination;
    }

    @JsonProperty("pagination")
    public void setPagination(Pagination pagination) {
        this.pagination = pagination;
    }

    @JsonProperty("trades")
    public List<Trade> getTrades() {
        return trades;
    }

    @JsonProperty("trades")
    public void setTrades(List<Trade> trades) {
        this.trades = trades;
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