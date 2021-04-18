package space.xrapid.domain.xumm.webhook;

import com.fasterxml.jackson.annotation.*;

import java.util.HashMap;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "app",
        "web"
})
public class ReturnUrl {

    @JsonProperty("app")
    private Object app;
    @JsonProperty("web")
    private Object web;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("app")
    public Object getApp() {
        return app;
    }

    @JsonProperty("app")
    public void setApp(Object app) {
        this.app = app;
    }

    @JsonProperty("web")
    public Object getWeb() {
        return web;
    }

    @JsonProperty("web")
    public void setWeb(Object web) {
        this.web = web;
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
