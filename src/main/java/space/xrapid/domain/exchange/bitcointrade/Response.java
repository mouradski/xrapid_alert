package space.xrapid.domain.exchange.bitcointrade;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
public class Response {

    @JsonProperty("code")
    private Object code;
    @JsonProperty("message")
    private Object message;
    @JsonProperty("data")
    private Data data;
}