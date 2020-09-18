package space.xrapid.domain.xumm;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Payload {

  @JsonProperty("txjson")
  private XummPayment txjson;


  @JsonProperty("custom_meta")
  private CustomMeta customMeta;
}
