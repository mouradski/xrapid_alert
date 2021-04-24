package space.xrapid.domain.exchange.braziliex;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
public class Trade {
    @JsonProperty("_id")
    private String id;
    @JsonProperty("date_exec")
    private String dateExec;
    @JsonProperty("type")
    private String type;
    @JsonProperty("amount")
    private String amount;
    @JsonProperty("price")
    private String price;
    @JsonProperty("total")
    private String total;
    @JsonProperty("timestamp")
    private Long timestamp;
}
