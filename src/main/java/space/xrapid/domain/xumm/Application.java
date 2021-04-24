package space.xrapid.domain.xumm;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
public class Application {
    @JsonProperty("name")
    private String name;
    @JsonProperty("description")
    private String description;
    @JsonProperty("disabled")
    private Integer disabled;
    @JsonProperty("uuidv4")
    private String uuidv4;
    @JsonProperty("icon_url")
    private String iconUrl;
    @JsonProperty("issued_user_token")
    private Object issuedUserToken;
}
