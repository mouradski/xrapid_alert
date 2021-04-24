package space.xrapid.domain.xumm;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
public class RequestJson {
    @JsonProperty("TransactionType")
    private String transactionType;
    @JsonProperty("Destination")
    private String destination;
    @JsonProperty("Fee")
    private String fee;
    @JsonProperty("Amount")
    private Amount amount;
}
