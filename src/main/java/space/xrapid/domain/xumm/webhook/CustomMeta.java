package space.xrapid.domain.xumm.webhook;

import com.fasterxml.jackson.annotation.*;

import java.util.HashMap;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "identifier",
        "blob",
        "instruction"
})
public class CustomMeta {

    @JsonProperty("identifier")
    private Object identifier;
    @JsonProperty("blob")
    private Object blob;
    @JsonProperty("instruction")
    private String instruction;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("identifier")
    public Object getIdentifier() {
        return identifier;
    }

    @JsonProperty("identifier")
    public void setIdentifier(Object identifier) {
        this.identifier = identifier;
    }

    @JsonProperty("blob")
    public Object getBlob() {
        return blob;
    }

    @JsonProperty("blob")
    public void setBlob(Object blob) {
        this.blob = blob;
    }

    @JsonProperty("instruction")
    public String getInstruction() {
        return instruction;
    }

    @JsonProperty("instruction")
    public void setInstruction(String instruction) {
        this.instruction = instruction;
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
