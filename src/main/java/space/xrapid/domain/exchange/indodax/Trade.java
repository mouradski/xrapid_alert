package space.xrapid.domain.exchange.indodax;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
public class Trade {
    @JsonProperty("date")
    private Long date;
    @JsonProperty("price")
    private double price;
    @JsonProperty("amount")
    private double amount;
    @JsonProperty("tid")
    private String tid;
    @JsonProperty("type")
    private String type;
}
