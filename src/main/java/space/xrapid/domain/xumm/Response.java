package space.xrapid.domain.xumm;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
public class Response {
    @JsonProperty("uuid")
    private String uuid;
    @JsonProperty("next")
    private Next next;
    @JsonProperty("refs")
    private Refs refs;
    @JsonProperty("pushed")
    private Boolean pushed;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();
}
