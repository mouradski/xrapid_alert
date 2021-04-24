package space.xrapid.domain.exchange.mercadobitcoin;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
public class Trade {
    @JsonProperty("tid")
    private Integer tid;
    @JsonProperty("date")
    private Long date;
    @JsonProperty("type")
    private String type;
    @JsonProperty("price")
    private Double price;
    @JsonProperty("amount")
    private Double amount;
}
