package space.xrapid.domain.exchange.novadax;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
public class Datum {
    @JsonProperty("amount")
    private double amount;
    @JsonProperty("price")
    private double price;
    @JsonProperty("side")
    private String side;
    @JsonProperty("timestamp")
    private long timestamp;
}