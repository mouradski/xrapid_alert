package space.xrapid.domain.bitso;

import com.fasterxml.jackson.annotation.*;

import java.util.HashMap;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
"success",
"payload"
})
public class BitsoResponse {

@JsonProperty("success")
private Boolean success;
@JsonProperty("payload")
private Payload payload;
@JsonIgnore
private Map<String, Object> additionalProperties = new HashMap<String, Object>();

@JsonProperty("success")
public Boolean getSuccess() {
return success;
}

@JsonProperty("success")
public void setSuccess(Boolean success) {
this.success = success;
}

@JsonProperty("payload")
public Payload getPayload() {
return payload;
}

@JsonProperty("payload")
public void setPayload(Payload payload) {
this.payload = payload;
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
