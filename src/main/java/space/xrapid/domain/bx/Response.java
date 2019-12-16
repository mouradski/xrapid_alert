package space.xrapid.domain.bx;

import com.fasterxml.jackson.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "trades",
        "lowask",
        "highbid",
        "user_orders"
})
public class Response {

    @JsonProperty("trades")
    private List<Trade> trades = null;
    @JsonProperty("lowask")
    private List<Object> lowask = null;
    @JsonProperty("highbid")
    private List<Object> highbid = null;
    @JsonProperty("user_orders")
    private List<Object> userOrders = null;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("trades")
    public List<Trade> getTrades() {
        return trades;
    }

    @JsonProperty("trades")
    public void setTrades(List<Trade> trades) {
        this.trades = trades;
    }

    @JsonProperty("lowask")
    public List<Object> getLowask() {
        return lowask;
    }

    @JsonProperty("lowask")
    public void setLowask(List<Object> lowask) {
        this.lowask = lowask;
    }

    @JsonProperty("highbid")
    public List<Object> getHighbid() {
        return highbid;
    }

    @JsonProperty("highbid")
    public void setHighbid(List<Object> highbid) {
        this.highbid = highbid;
    }

    @JsonProperty("user_orders")
    public List<Object> getUserOrders() {
        return userOrders;
    }

    @JsonProperty("user_orders")
    public void setUserOrders(List<Object> userOrders) {
        this.userOrders = userOrders;
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
