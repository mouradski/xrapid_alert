package space.xrapid.domain.exchange.currencycom;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
public class Trade {

    @JsonProperty("a")
    private String a;
    @JsonProperty("p")
    private double p;
    @JsonProperty("q")
    private double q;
    @JsonProperty("T")
    private long t;
    @JsonProperty("m")
    private Boolean m;
}
