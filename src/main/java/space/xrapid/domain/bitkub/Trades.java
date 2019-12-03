package space.xrapid.domain.bitkub;

import com.fasterxml.jackson.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "error",
        "result"
})
public class Trades {

    @JsonProperty("error")
    private Integer error;
    @JsonProperty("result")
    private List<List<String>> result = null;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("error")
    public Integer getError() {
        return error;
    }

    @JsonProperty("error")
    public void setError(Integer error) {
        this.error = error;
    }

    @JsonProperty("result")
    public List<List<String>> getResult() {
        return result;
    }

    @JsonProperty("result")
    public void setResult(List<List<String>> result) {
        this.result = result;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}