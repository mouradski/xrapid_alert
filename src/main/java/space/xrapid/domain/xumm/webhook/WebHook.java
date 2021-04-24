package space.xrapid.domain.xumm.webhook;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
public class WebHook {
    @JsonProperty("meta")
    private Meta meta;
    @JsonProperty("custom_meta")
    private CustomMeta customMeta;
    @JsonProperty("payloadResponse")
    private PayloadResponse payloadResponse;
    @JsonProperty("userToken")
    private UserToken userToken;
}
