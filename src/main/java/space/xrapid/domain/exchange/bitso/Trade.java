package space.xrapid.domain.exchange.bitso;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.ToString;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@ToString
public class Trade {
    @JsonProperty("book")
    private String book;
    @JsonProperty("created_at")
    private String createdAt;
    @JsonProperty("amount")
    private String amount;
    @JsonProperty("maker_side")
    private String makerSide;
    @JsonProperty("price")
    private String price;
    @JsonProperty("tid")
    private Integer tid;
}
