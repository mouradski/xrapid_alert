package space.xrapid.domain.exchange.coinone;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
public class CompleteOrder {
    @JsonProperty("is_ask")
    private String isAsk;
    @JsonProperty("timestamp")
    private Long timestamp;
    @JsonProperty("price")
    private double price;
    @JsonProperty("id")
    private String id;
    @JsonProperty("qty")
    private double qty;
}
