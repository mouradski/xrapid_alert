package space.xrapid.domain.exchange.bitcointrade;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
public class Data {

    @JsonProperty("pagination")
    private Pagination pagination;
    @JsonProperty("trades")
    private List<Trade> trades = null;
}