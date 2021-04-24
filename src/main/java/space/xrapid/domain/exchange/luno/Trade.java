package space.xrapid.domain.exchange.luno;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
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
}