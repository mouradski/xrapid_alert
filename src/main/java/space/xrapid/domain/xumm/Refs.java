package space.xrapid.domain.xumm;

import com.fasterxml.jackson.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "qr_png",
        "qr_matrix",
        "qr_uri_quality_opts",
        "websocket_status"
})
public class Refs {

    @JsonProperty("qr_png")
    private String qrPng;
    @JsonProperty("qr_matrix")
    private String qrMatrix;
    @JsonProperty("qr_uri_quality_opts")
    private List<String> qrUriQualityOpts = null;
    @JsonProperty("websocket_status")
    private String websocketStatus;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("qr_png")
    public String getQrPng() {
        return qrPng;
    }

    @JsonProperty("qr_png")
    public void setQrPng(String qrPng) {
        this.qrPng = qrPng;
    }

    @JsonProperty("qr_matrix")
    public String getQrMatrix() {
        return qrMatrix;
    }

    @JsonProperty("qr_matrix")
    public void setQrMatrix(String qrMatrix) {
        this.qrMatrix = qrMatrix;
    }

    @JsonProperty("qr_uri_quality_opts")
    public List<String> getQrUriQualityOpts() {
        return qrUriQualityOpts;
    }

    @JsonProperty("qr_uri_quality_opts")
    public void setQrUriQualityOpts(List<String> qrUriQualityOpts) {
        this.qrUriQualityOpts = qrUriQualityOpts;
    }

    @JsonProperty("websocket_status")
    public String getWebsocketStatus() {
        return websocketStatus;
    }

    @JsonProperty("websocket_status")
    public void setWebsocketStatus(String websocketStatus) {
        this.websocketStatus = websocketStatus;
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
