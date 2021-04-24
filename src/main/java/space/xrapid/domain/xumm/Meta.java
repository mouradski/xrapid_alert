package space.xrapid.domain.xumm;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
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
}
