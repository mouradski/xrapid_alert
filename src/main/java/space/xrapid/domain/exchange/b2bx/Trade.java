package space.xrapid.domain.exchange.b2bx;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
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
}