package space.xrapid.domain.exchange.bitbank;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
public class Transaction {

    @JsonProperty("transaction_id")
    private Long transactionId;
    @JsonProperty("side")
    private String side;
    @JsonProperty("price")
    private double price;
    @JsonProperty("amount")
    private double amount;
    @JsonProperty("executed_at")
    private Long executedAt;
}
