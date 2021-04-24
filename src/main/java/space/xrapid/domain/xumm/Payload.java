package space.xrapid.domain.xumm;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

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
