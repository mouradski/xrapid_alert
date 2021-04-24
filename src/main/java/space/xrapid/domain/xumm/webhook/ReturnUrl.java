package space.xrapid.domain.xumm.webhook;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
public class ReturnUrl {
    @JsonProperty("app")
    private Object app;
    @JsonProperty("web")
    private Object web;
}
