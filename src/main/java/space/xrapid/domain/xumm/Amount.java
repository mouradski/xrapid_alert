package space.xrapid.domain.xumm;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Builder
public class Amount {
    @JsonProperty("currency")
    private String currency;
    @JsonProperty("value")
    private double value;
    @JsonProperty("issuer")
    private String issuer;
}
