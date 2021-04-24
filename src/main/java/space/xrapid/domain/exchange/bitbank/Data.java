package space.xrapid.domain.exchange.bitbank;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
public class Data {
    @JsonProperty("transactions")
    private List<Transaction> transactions = null;
}
