package space.xrapid.domain.exchange.independentreserve;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
public class Trades {
    @JsonProperty("Trades")
    private List<Trade> trades = null;
    @JsonProperty("PrimaryCurrencyCode")
    private String primaryCurrencyCode;
    @JsonProperty("SecondaryCurrencyCode")
    private String secondaryCurrencyCode;
    @JsonProperty("CreatedTimestampUtc")
    private String createdTimestampUtc;
}