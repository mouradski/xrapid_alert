package space.xrapid.domain.exchange.coinone;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
public class Trades {
    @JsonProperty("errorCode")
    private String errorCode;
    @JsonProperty("timestamp")
    private String timestamp;
    @JsonProperty("completeOrders")
    private List<CompleteOrder> completeOrders = null;
    @JsonProperty("result")
    private String result;
    @JsonProperty("currency")
    private String currency;
}
