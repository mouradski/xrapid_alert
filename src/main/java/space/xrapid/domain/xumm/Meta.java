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
    "exists",
    "uuid",
    "multisign",
    "submit",
    "destination",
    "resolved_destination",
    "resolved",
    "signed",
    "cancelled",
    "expired",
    "pushed",
    "app_opened",
    "return_url_app",
    "return_url_web"
})
public class Meta {

  @JsonProperty("exists")
  private Boolean exists;
  @JsonProperty("uuid")
  private String uuid;
  @JsonProperty("multisign")
  private Boolean multisign;
  @JsonProperty("submit")
  private Boolean submit;
  @JsonProperty("destination")
  private String destination;
  @JsonProperty("resolved_destination")
  private String resolvedDestination;
  @JsonProperty("resolved")
  private Boolean resolved;
  @JsonProperty("signed")
  private Boolean signed;
  @JsonProperty("cancelled")
  private Boolean cancelled;
  @JsonProperty("expired")
  private Boolean expired;
  @JsonProperty("pushed")
  private Boolean pushed;
  @JsonProperty("app_opened")
  private Boolean appOpened;
  @JsonProperty("return_url_app")
  private Object returnUrlApp;
  @JsonProperty("return_url_web")
  private Object returnUrlWeb;
  @JsonIgnore
  private Map<String, Object> additionalProperties = new HashMap<String, Object>();

  @JsonProperty("exists")
  public Boolean getExists() {
    return exists;
  }

  @JsonProperty("exists")
  public void setExists(Boolean exists) {
    this.exists = exists;
  }

  @JsonProperty("uuid")
  public String getUuid() {
    return uuid;
  }

  @JsonProperty("uuid")
  public void setUuid(String uuid) {
    this.uuid = uuid;
  }

  @JsonProperty("multisign")
  public Boolean getMultisign() {
    return multisign;
  }

  @JsonProperty("multisign")
  public void setMultisign(Boolean multisign) {
    this.multisign = multisign;
  }

  @JsonProperty("submit")
  public Boolean getSubmit() {
    return submit;
  }

  @JsonProperty("submit")
  public void setSubmit(Boolean submit) {
    this.submit = submit;
  }

  @JsonProperty("destination")
  public String getDestination() {
    return destination;
  }

  @JsonProperty("destination")
  public void setDestination(String destination) {
    this.destination = destination;
  }

  @JsonProperty("resolved_destination")
  public String getResolvedDestination() {
    return resolvedDestination;
  }

  @JsonProperty("resolved_destination")
  public void setResolvedDestination(String resolvedDestination) {
    this.resolvedDestination = resolvedDestination;
  }

  @JsonProperty("resolved")
  public Boolean getResolved() {
    return resolved;
  }

  @JsonProperty("resolved")
  public void setResolved(Boolean resolved) {
    this.resolved = resolved;
  }

  @JsonProperty("signed")
  public Boolean getSigned() {
    return signed;
  }

  @JsonProperty("signed")
  public void setSigned(Boolean signed) {
    this.signed = signed;
  }

  @JsonProperty("cancelled")
  public Boolean getCancelled() {
    return cancelled;
  }

  @JsonProperty("cancelled")
  public void setCancelled(Boolean cancelled) {
    this.cancelled = cancelled;
  }

  @JsonProperty("expired")
  public Boolean getExpired() {
    return expired;
  }

  @JsonProperty("expired")
  public void setExpired(Boolean expired) {
    this.expired = expired;
  }

  @JsonProperty("pushed")
  public Boolean getPushed() {
    return pushed;
  }

  @JsonProperty("pushed")
  public void setPushed(Boolean pushed) {
    this.pushed = pushed;
  }

  @JsonProperty("app_opened")
  public Boolean getAppOpened() {
    return appOpened;
  }

  @JsonProperty("app_opened")
  public void setAppOpened(Boolean appOpened) {
    this.appOpened = appOpened;
  }

  @JsonProperty("return_url_app")
  public Object getReturnUrlApp() {
    return returnUrlApp;
  }

  @JsonProperty("return_url_app")
  public void setReturnUrlApp(Object returnUrlApp) {
    this.returnUrlApp = returnUrlApp;
  }

  @JsonProperty("return_url_web")
  public Object getReturnUrlWeb() {
    return returnUrlWeb;
  }

  @JsonProperty("return_url_web")
  public void setReturnUrlWeb(Object returnUrlWeb) {
    this.returnUrlWeb = returnUrlWeb;
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
