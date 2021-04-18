package space.xrapid.domain.xumm.webhook;

import com.fasterxml.jackson.annotation.*;

import java.util.HashMap;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "user_token",
        "token_issued",
        "token_expiration"
})
public class UserToken {

    @JsonProperty("user_token")
    private String userToken;
    @JsonProperty("token_issued")
    private String tokenIssued;
    @JsonProperty("token_expiration")
    private String tokenExpiration;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("user_token")
    public String getUserToken() {
        return userToken;
    }

    @JsonProperty("user_token")
    public void setUserToken(String userToken) {
        this.userToken = userToken;
    }

    @JsonProperty("token_issued")
    public String getTokenIssued() {
        return tokenIssued;
    }

    @JsonProperty("token_issued")
    public void setTokenIssued(String tokenIssued) {
        this.tokenIssued = tokenIssued;
    }

    @JsonProperty("token_expiration")
    public String getTokenExpiration() {
        return tokenExpiration;
    }

    @JsonProperty("token_expiration")
    public void setTokenExpiration(String tokenExpiration) {
        this.tokenExpiration = tokenExpiration;
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
