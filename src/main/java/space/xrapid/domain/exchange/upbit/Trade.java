package space.xrapid.domain.exchange.upbit;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
public class Trade {
    @JsonProperty("timestamp")
    private Long timestamp;
    @JsonProperty("trade_price")
    private Double tradePrice;
    @JsonProperty("trade_volume")
    private Double tradeVolume;
    @JsonProperty("ask_bid")
    private String askBid;
    @JsonProperty("sequential_id")
    private String sequentialId;
}
