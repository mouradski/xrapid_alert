package space.xrapid.domain.xumm.webhook;

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
    "payload_uuidv4",
    "reference_call_uuidv4",
    "signed",
    "user_token",
    "return_url"
})
public class PayloadResponse {

  @JsonProperty("payload_uuidv4")
  private String payloadUuidv4;
  @JsonProperty("reference_call_uuidv4")
  private String referenceCallUuidv4;
  @JsonProperty("signed")
  private Boolean signed;
  @JsonProperty("user_token")
  private Boolean userToken;
  @JsonProperty("return_url")
  private ReturnUrl returnUrl;
  @JsonIgnore
  private Map<String, Object> additionalProperties = new HashMap<String, Object>();

  @JsonProperty("payload_uuidv4")
  public String getPayloadUuidv4() {
    return payloadUuidv4;
  }

  @JsonProperty("payload_uuidv4")
  public void setPayloadUuidv4(String payloadUuidv4) {
    this.payloadUuidv4 = payloadUuidv4;
  }

  @JsonProperty("reference_call_uuidv4")
  public String getReferenceCallUuidv4() {
    return referenceCallUuidv4;
  }

  @JsonProperty("reference_call_uuidv4")
  public void setReferenceCallUuidv4(String referenceCallUuidv4) {
    this.referenceCallUuidv4 = referenceCallUuidv4;
  }

  @JsonProperty("signed")
  public Boolean getSigned() {
    return signed;
  }

  @JsonProperty("signed")
  public void setSigned(Boolean signed) {
    this.signed = signed;
  }

  @JsonProperty("user_token")
  public Boolean getUserToken() {
    return userToken;
  }

  @JsonProperty("user_token")
  public void setUserToken(Boolean userToken) {
    this.userToken = userToken;
  }

  @JsonProperty("return_url")
  public ReturnUrl getReturnUrl() {
    return returnUrl;
  }

  @JsonProperty("return_url")
  public void setReturnUrl(ReturnUrl returnUrl) {
    this.returnUrl = returnUrl;
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
