package space.xrapid.domain.exchange.bithumb;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
public class Trades {
    @JsonProperty("status")
    private String status;
    @JsonProperty("data")
    private List<Datum> data = null;
}