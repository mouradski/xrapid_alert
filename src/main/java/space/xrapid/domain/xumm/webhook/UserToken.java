package space.xrapid.domain.xumm.webhook;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
public class UserToken {
    @JsonProperty("user_token")
    private String userToken;
    @JsonProperty("token_issued")
    private String tokenIssued;
    @JsonProperty("token_expiration")
    private String tokenExpiration;
}
