package space.xrapid.domain.exchange.bx;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
public class Trade {
    @JsonProperty("trade_id")
    private String tradeId;
    @JsonProperty("rate")
    private String rate;
    @JsonProperty("amount")
    private String amount;
    @JsonProperty("trade_date")
    private String tradeDate;
    @JsonProperty("order_id")
    private String orderId;
    @JsonProperty("trade_type")
    private String tradeType;
    @JsonProperty("reference_id")
    private String referenceId;
    @JsonProperty("seconds")
    private Integer seconds;
}
