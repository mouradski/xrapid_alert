package space.xrapid.domain.exchange.coinfield;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
public class Trades {
    @JsonProperty("market")
    private String market;
    @JsonProperty("trades_hash")
    private String tradesHash;
    @JsonProperty("trades")
    private List<Trade> trades = null;
    @JsonProperty("timestamp")
    private String timestamp;
    @JsonProperty("took")
    private String took;
}
