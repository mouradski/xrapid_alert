package space.xrapid.domain.xumm.webhook;

import com.fasterxml.jackson.annotation.*;

import java.util.HashMap;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "url",
        "application_uuidv4",
        "payload_uuidv4"
})
public class Meta {

    @JsonProperty("url")
    private String url;
    @JsonProperty("application_uuidv4")
    private String applicationUuidv4;
    @JsonProperty("payload_uuidv4")
    private String payloadUuidv4;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("url")
    public String getUrl() {
        return url;
    }

    @JsonProperty("url")
    public void setUrl(String url) {
        this.url = url;
    }

    @JsonProperty("application_uuidv4")
    public String getApplicationUuidv4() {
        return applicationUuidv4;
    }

    @JsonProperty("application_uuidv4")
    public void setApplicationUuidv4(String applicationUuidv4) {
        this.applicationUuidv4 = applicationUuidv4;
    }

    @JsonProperty("payload_uuidv4")
    public String getPayloadUuidv4() {
        return payloadUuidv4;
    }

    @JsonProperty("payload_uuidv4")
    public void setPayloadUuidv4(String payloadUuidv4) {
        this.payloadUuidv4 = payloadUuidv4;
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
