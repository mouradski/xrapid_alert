package space.xrapid.domain.exchange.bitstamp;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
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
}