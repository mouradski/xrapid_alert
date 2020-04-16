package space.xrapid.domain.xumm;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Builder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "TransactionType",
        "Account",
        "Destination",
        "Amount",
        "DestinationTag"
})
@Builder
public class XummPayment {

    @JsonProperty("TransactionType")
    private String transactionType;
    @JsonProperty("Destination")
    private String destination;
    @JsonProperty("Amount")
    private Amount amount;

    @JsonProperty("TransactionType")
    public String getTransactionType() {
        return transactionType;
    }

    @JsonProperty("TransactionType")
    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    @JsonProperty("Destination")
    public String getDestination() {
        return destination;
    }

    @JsonProperty("Destination")
    public void setDestination(String destination) {
        this.destination = destination;
    }

    @JsonProperty("Amount")
    public Amount getAmount() {
        return amount;
    }

    @JsonProperty("Amount")
    public void setAmount(Amount amount) {
        this.amount = amount;
    }


}
