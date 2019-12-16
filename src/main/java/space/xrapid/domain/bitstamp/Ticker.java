package space.xrapid.domain.bitstamp;

import com.fasterxml.jackson.annotation.*;

import java.util.HashMap;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "high",
        "last",
        "timestamp",
        "bid",
        "vwap",
        "volume",
        "low",
        "ask",
        "open"
})
public class Ticker {

    @JsonProperty("high")
    private String high;
    @JsonProperty("last")
    private double last;
    @JsonProperty("timestamp")
    private String timestamp;
    @JsonProperty("bid")
    private String bid;
    @JsonProperty("vwap")
    private String vwap;
    @JsonProperty("volume")
    private String volume;
    @JsonProperty("low")
    private String low;
    @JsonProperty("ask")
    private String ask;
    @JsonProperty("open")
    private String open;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("high")
    public String getHigh() {
        return high;
    }

    @JsonProperty("high")
    public void setHigh(String high) {
        this.high = high;
    }

    @JsonProperty("last")
    public double getLast() {
        return last;
    }

    @JsonProperty("last")
    public void setLast(double last) {
        this.last = last;
    }

    @JsonProperty("timestamp")
    public String getTimestamp() {
        return timestamp;
    }

    @JsonProperty("timestamp")
    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    @JsonProperty("bid")
    public String getBid() {
        return bid;
    }

    @JsonProperty("bid")
    public void setBid(String bid) {
        this.bid = bid;
    }

    @JsonProperty("vwap")
    public String getVwap() {
        return vwap;
    }

    @JsonProperty("vwap")
    public void setVwap(String vwap) {
        this.vwap = vwap;
    }

    @JsonProperty("volume")
    public String getVolume() {
        return volume;
    }

    @JsonProperty("volume")
    public void setVolume(String volume) {
        this.volume = volume;
    }

    @JsonProperty("low")
    public String getLow() {
        return low;
    }

    @JsonProperty("low")
    public void setLow(String low) {
        this.low = low;
    }

    @JsonProperty("ask")
    public String getAsk() {
        return ask;
    }

    @JsonProperty("ask")
    public void setAsk(String ask) {
        this.ask = ask;
    }

    @JsonProperty("open")
    public String getOpen() {
        return open;
    }

    @JsonProperty("open")
    public void setOpen(String open) {
        this.open = open;
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