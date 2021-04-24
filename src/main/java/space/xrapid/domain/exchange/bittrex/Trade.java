package space.xrapid.domain.exchange.bittrex;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
public class Trade {
    @JsonProperty("Id")
    private Integer id;
    @JsonProperty("TimeStamp")
    private String timeStamp;
    @JsonProperty("Quantity")
    private Double quantity;
    @JsonProperty("Price")
    private Double price;
    @JsonProperty("Total")
    private Double total;
    @JsonProperty("FillType")
    private String fillType;
    @JsonProperty("OrderType")
    private String orderType;
    @JsonProperty("Uuid")
    private String uuid;
}