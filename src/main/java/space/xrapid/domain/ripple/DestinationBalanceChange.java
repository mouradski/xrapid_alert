package space.xrapid.domain.ripple;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
public class DestinationBalanceChange {
    @JsonProperty("counterparty")
    private String counterparty;
    @JsonProperty("currency")
    private String currency;
    @JsonProperty("value")
    private String value;
}
