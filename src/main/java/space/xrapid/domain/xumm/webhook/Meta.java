package space.xrapid.domain.xumm.webhook;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
public class Meta {
    @JsonProperty("url")
    private String url;
    @JsonProperty("application_uuidv4")
    private String applicationUuidv4;
    @JsonProperty("payload_uuidv4")
    private String payloadUuidv4;
}
