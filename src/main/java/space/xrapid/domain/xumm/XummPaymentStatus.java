package space.xrapid.domain.xumm;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
public class XummPaymentStatus {
    @JsonProperty("meta")
    private Meta meta;
    @JsonProperty("application")
    private Application application;
    @JsonProperty("payload")
    private Payload payload;
    @JsonProperty("response")
    private Response response;
    @JsonProperty("custom_meta")
    private CustomMeta customMeta;
}
