package space.xrapid.domain.xumm.webhook;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
public class CustomMeta {
    @JsonProperty("identifier")
    private Object identifier;
    @JsonProperty("blob")
    private Object blob;
    @JsonProperty("instruction")
    private String instruction;
}
