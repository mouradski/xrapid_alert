package space.xrapid.domain.exchange.bitkub;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
public class Trades {
    @JsonProperty("error")
    private Integer error;
    @JsonProperty("result")
    private List<List<String>> result = null;
}