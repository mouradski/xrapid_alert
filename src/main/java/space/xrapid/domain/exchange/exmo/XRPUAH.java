package space.xrapid.domain.exchange.exmo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
public class XRPUAH {
    @JsonProperty("trade_id")
    private String tradeId;
    @JsonProperty("type")
    private String type;
    @JsonProperty("quantity")
    private Double quantity;
    @JsonProperty("price")
    private Double price;
    @JsonProperty("amount")
    private Double amount;
    @JsonProperty("date")
    private Long date;
}
