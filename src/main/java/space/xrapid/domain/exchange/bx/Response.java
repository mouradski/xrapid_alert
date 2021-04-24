package space.xrapid.domain.exchange.bx;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
public class Response {
    @JsonProperty("trades")
    private List<Trade> trades = null;
    @JsonProperty("lowask")
    private List<Object> lowask = null;
    @JsonProperty("highbid")
    private List<Object> highbid = null;
    @JsonProperty("user_orders")
    private List<Object> userOrders = null;
}
