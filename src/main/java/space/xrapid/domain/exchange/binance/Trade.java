package space.xrapid.domain.exchange.binance;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
public class Trade {
    @JsonProperty("id")
    private Integer id;
    @JsonProperty("price")
    private Double price;
    @JsonProperty("qty")
    private Double qty;
    @JsonProperty("quoteQty")
    private Double quoteQty;
    @JsonProperty("time")
    private Long time;
    @JsonProperty("isBuyerMaker")
    private Boolean isBuyerMaker;
    @JsonProperty("isBestMatch")
    private Boolean isBestMatch;
}