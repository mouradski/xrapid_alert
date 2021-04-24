package space.xrapid.domain.exchange.valr;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
public class Trade {
    @JsonProperty("price")
    private String price;
    @JsonProperty("quantity")
    private String quantity;
    @JsonProperty("currencyPair")
    private String currencyPair;
    @JsonProperty("tradedAt")
    private String tradedAt;
    @JsonProperty("takerSide")
    private String takerSide;
    @JsonProperty("sequenceId")
    private Integer sequenceId;
    @JsonProperty("id")
    private String id;
    @JsonProperty("quoteVolume")
    private String quoteVolume;
}
