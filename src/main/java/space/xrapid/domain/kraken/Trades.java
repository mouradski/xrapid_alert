package space.xrapid.domain.kraken;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
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
  private List<Object> error = null;
  @JsonProperty("result")
  private Result result;
  @JsonIgnore
  private Map<String, Object> additionalProperties = new HashMap<String, Object>();

  @JsonProperty("error")
  public List<Object> getError() {
    return error;
  }

  @JsonProperty("error")
  public void setError(List<Object> error) {
    this.error = error;
  }

  @JsonProperty("result")
  public Result getResult() {
    return result;
  }

  @JsonProperty("result")
  public void setResult(Result result) {
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