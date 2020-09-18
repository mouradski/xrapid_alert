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
    "name",
    "description",
    "disabled",
    "uuidv4",
    "icon_url",
    "issued_user_token"
})
public class Application {

  @JsonProperty("name")
  private String name;
  @JsonProperty("description")
  private String description;
  @JsonProperty("disabled")
  private Integer disabled;
  @JsonProperty("uuidv4")
  private String uuidv4;
  @JsonProperty("icon_url")
  private String iconUrl;
  @JsonProperty("issued_user_token")
  private Object issuedUserToken;
  @JsonIgnore
  private Map<String, Object> additionalProperties = new HashMap<String, Object>();

  @JsonProperty("name")
  public String getName() {
    return name;
  }

  @JsonProperty("name")
  public void setName(String name) {
    this.name = name;
  }

  @JsonProperty("description")
  public String getDescription() {
    return description;
  }

  @JsonProperty("description")
  public void setDescription(String description) {
    this.description = description;
  }

  @JsonProperty("disabled")
  public Integer getDisabled() {
    return disabled;
  }

  @JsonProperty("disabled")
  public void setDisabled(Integer disabled) {
    this.disabled = disabled;
  }

  @JsonProperty("uuidv4")
  public String getUuidv4() {
    return uuidv4;
  }

  @JsonProperty("uuidv4")
  public void setUuidv4(String uuidv4) {
    this.uuidv4 = uuidv4;
  }

  @JsonProperty("icon_url")
  public String getIconUrl() {
    return iconUrl;
  }

  @JsonProperty("icon_url")
  public void setIconUrl(String iconUrl) {
    this.iconUrl = iconUrl;
  }

  @JsonProperty("issued_user_token")
  public Object getIssuedUserToken() {
    return issuedUserToken;
  }

  @JsonProperty("issued_user_token")
  public void setIssuedUserToken(Object issuedUserToken) {
    this.issuedUserToken = issuedUserToken;
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
