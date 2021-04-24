package space.xrapid.domain.exchange.independentreserve;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
public class Trade {
    @JsonProperty("TradeTimestampUtc")
    private String tradeTimestampUtc;
    @JsonProperty("PrimaryCurrencyAmount")
    private Double primaryCurrencyAmount;
    @JsonProperty("SecondaryCurrencyTradePrice")
    private Double secondaryCurrencyTradePrice;
}