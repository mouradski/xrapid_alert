package space.xrapid.domain.exchange.novadax;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
public class Trades {
    @JsonProperty("code")
    private String code;
    @JsonProperty("data")
    private List<Datum> data = null;
    @JsonProperty("message")
    private String message;
}