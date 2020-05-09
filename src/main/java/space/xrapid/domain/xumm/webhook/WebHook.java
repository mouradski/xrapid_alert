package space.xrapid.domain.xumm.webhook;

import com.fasterxml.jackson.annotation.*;

import java.util.HashMap;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "meta",
        "custom_meta",
        "payloadResponse",
        "userToken"
})
public class WebHook {

    @JsonProperty("meta")
    private Meta meta;
    @JsonProperty("custom_meta")
    private CustomMeta customMeta;
    @JsonProperty("payloadResponse")
    private PayloadResponse payloadResponse;
    @JsonProperty("userToken")
    private UserToken userToken;
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

    @JsonProperty("custom_meta")
    public CustomMeta getCustomMeta() {
        return customMeta;
    }

    @JsonProperty("custom_meta")
    public void setCustomMeta(CustomMeta customMeta) {
        this.customMeta = customMeta;
    }

    @JsonProperty("payloadResponse")
    public PayloadResponse getPayloadResponse() {
        return payloadResponse;
    }

    @JsonProperty("payloadResponse")
    public void setPayloadResponse(PayloadResponse payloadResponse) {
        this.payloadResponse = payloadResponse;
    }

    @JsonProperty("userToken")
    public UserToken getUserToken() {
        return userToken;
    }

    @JsonProperty("userToken")
    public void setUserToken(UserToken userToken) {
        this.userToken = userToken;
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
