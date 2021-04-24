package space.xrapid.domain.exchange.bithumb;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
public class Datum {

    @JsonProperty("transaction_date")
    private String transactionDate;
    @JsonProperty("type")
    private String type;
    @JsonProperty("units_traded")
    private double unitsTraded;
    @JsonProperty("price")
    private double price;
    @JsonProperty("total")
    private double total;
}