package space.xrapid.domain.exchange.cexio;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
public class Trade {
    @JsonProperty("type")
    private String type;
    @JsonProperty("date")
    private long date;
    @JsonProperty("amount")
    private double amount;
    @JsonProperty("price")
    private double price;
    @JsonProperty("tid")
    private String tid;
}