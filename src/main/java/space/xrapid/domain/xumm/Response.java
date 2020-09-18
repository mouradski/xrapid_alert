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
    "uuid",
    "next",
    "refs",
    "pushed"
})
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

  @JsonProperty("uuid")
  public String getUuid() {
    return uuid;
  }

  @JsonProperty("uuid")
  public void setUuid(String uuid) {
    this.uuid = uuid;
  }

  @JsonProperty("next")
  public Next getNext() {
    return next;
  }

  @JsonProperty("next")
  public void setNext(Next next) {
    this.next = next;
  }

  @JsonProperty("refs")
  public Refs getRefs() {
    return refs;
  }

  @JsonProperty("refs")
  public void setRefs(Refs refs) {
    this.refs = refs;
  }

  @JsonProperty("pushed")
  public Boolean getPushed() {
    return pushed;
  }

  @JsonProperty("pushed")
  public void setPushed(Boolean pushed) {
    this.pushed = pushed;
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
