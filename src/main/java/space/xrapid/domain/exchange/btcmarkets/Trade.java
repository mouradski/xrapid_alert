package space.xrapid.domain.exchange.btcmarkets;

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
    @JsonProperty("amount")
    private double amount;
    @JsonProperty("timestamp")
    private String timestamp;
    @JsonProperty("side")
    private String side;
}