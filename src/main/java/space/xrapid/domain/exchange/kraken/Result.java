package space.xrapid.domain.exchange.kraken;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
public class Result {
    @JsonProperty("last")
    private String last;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();
}