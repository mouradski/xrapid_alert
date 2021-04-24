package space.xrapid.domain.exchange.bitcointrade;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
public class Trade {
    @JsonProperty("type")
    private String type;
    @JsonProperty("amount")
    private Double amount;
    @JsonProperty("unit_price")
    private Double unitPrice;
    @JsonProperty("active_order_code")
    private String activeOrderCode;
    @JsonProperty("passive_order_code")
    private String passiveOrderCode;
    @JsonProperty("date")
    private String date;
}