package space.xrapid.domain.exchange.coinfield;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
public class Trade {
    @JsonProperty("id")
    private String id;
    @JsonProperty("price")
    private double price;
    @JsonProperty("volume")
    private double volume;
    @JsonProperty("total_value")
    private double totalValue;
    @JsonProperty("timestamp")
    private String timestamp;
}
