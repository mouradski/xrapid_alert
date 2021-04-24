package space.xrapid.domain.exchange.bitbank;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
public class Transactions {

    @JsonProperty("success")
    private Integer success;
    @JsonProperty("data")
    private Data data;
}
