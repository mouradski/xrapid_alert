package space.xrapid.domain.xumm;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.util.HashMap;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "meta",
    "application",
    "payload",
    "response",
    "custom_meta"
})
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
  @JsonIgnore
  private Map<String, Object> additionalProperties = new HashMap<String, Object>();

  @JsonProperty("meta")
  public Meta getMeta() {
    return meta;
  }

  @JsonProperty("meta")
  public void setMeta(Meta meta) {
    this.meta = meta;
  }

  @JsonProperty("application")
  public Application getApplication() {
    return application;
  }

  @JsonProperty("application")
  public void setApplication(Application application) {
    this.application = application;
  }

  @JsonProperty("payload")
  public Payload getPayload() {
    return payload;
  }

  @JsonProperty("payload")
  public void setPayload(Payload payload) {
    this.payload = payload;
  }

  @JsonProperty("response")
  public Response getResponse() {
    return response;
  }

  @JsonProperty("response")
  public void setResponse(Response response) {
    this.response = response;
  }

  @JsonProperty("custom_meta")
  public CustomMeta getCustomMeta() {
    return customMeta;
  }

  @JsonProperty("custom_meta")
  public void setCustomMeta(CustomMeta customMeta) {
    this.customMeta = customMeta;
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
