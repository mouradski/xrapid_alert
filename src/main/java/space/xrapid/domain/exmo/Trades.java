package space.xrapid.domain.exmo;

import com.fasterxml.jackson.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "XRP_UAH"
})
public class Trades {

    @JsonProperty("XRP_UAH")
    private List<XRPUAH> xRPUAH = null;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("XRP_UAH")
    public List<XRPUAH> getXRPUAH() {
        return xRPUAH;
    }

    @JsonProperty("XRP_UAH")
    public void setXRPUAH(List<XRPUAH> xRPUAH) {
        this.xRPUAH = xRPUAH;
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
