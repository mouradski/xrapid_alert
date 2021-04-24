package space.xrapid.domain.exchange.bitso;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
public class BitsoXrpTrades {
    @JsonProperty("success")
    private Boolean success;
    @JsonProperty("payload")
    private List<Trade> payment = null;
}
