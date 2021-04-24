package space.xrapid.domain.xumm.webhook;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
public class PayloadResponse {
    @JsonProperty("payload_uuidv4")
    private String payloadUuidv4;
    @JsonProperty("reference_call_uuidv4")
    private String referenceCallUuidv4;
    @JsonProperty("signed")
    private Boolean signed;
    @JsonProperty("user_token")
    private Boolean userToken;
    @JsonProperty("return_url")
    private ReturnUrl returnUrl;
}
