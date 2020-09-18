package space.xrapid.domain.xumm;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Builder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "currency",
    "value",
    "issuer"
})
@Builder
public class Amount {

  @JsonProperty("currency")
  private String currency;
  @JsonProperty("value")
  private double value;
  @JsonProperty("issuer")
  private String issuer;

}
