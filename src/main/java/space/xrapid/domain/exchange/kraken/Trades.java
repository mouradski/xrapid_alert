package space.xrapid.domain.exchange.kraken;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
public class Trades {
    @JsonProperty("error")
    private List<Object> error = null;
    @JsonProperty("result")
    private Result result;
}