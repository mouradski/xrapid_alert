package space.xrapid.domain.ripple;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
public class Payments {
    @JsonProperty("result")
    private String result;
    @JsonProperty("count")
    private Integer count;
    @JsonProperty("marker")
    private String marker;
    @JsonProperty("payments")
    private List<Payment> payments = null;
}
