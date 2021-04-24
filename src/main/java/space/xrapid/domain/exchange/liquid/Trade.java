package space.xrapid.domain.exchange.liquid;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
public class Trade {
    @JsonProperty("id")
    private String id;
    @JsonProperty("quantity")
    private double quantity;
    @JsonProperty("price")
    private double price;
    @JsonProperty("taker_side")
    private String takerSide;
    @JsonProperty("created_at")
    private Long createdAt;
}