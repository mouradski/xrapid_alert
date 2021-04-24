package space.xrapid.domain.exchange.bitstamp;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
public class Trade {
    @JsonProperty("date")
    private Long date;
    @JsonProperty("tid")
    private String tid;
    @JsonProperty("price")
    private String price;
    @JsonProperty("type")
    private String type;
    @JsonProperty("amount")
    private String amount;
}
