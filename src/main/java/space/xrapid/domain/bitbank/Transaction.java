package space.xrapid.domain.bitbank;

import com.fasterxml.jackson.annotation.*;

import java.util.HashMap;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "transaction_id",
        "side",
        "price",
        "amount",
        "executed_at"
})
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
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("transaction_id")
    public Long getTransactionId() {
        return transactionId;
    }

    @JsonProperty("transaction_id")
    public void setTransactionId(Long transactionId) {
        this.transactionId = transactionId;
    }

    @JsonProperty("side")
    public String getSide() {
        return side;
    }

    @JsonProperty("side")
    public void setSide(String side) {
        this.side = side;
    }

    @JsonProperty("price")
    public double getPrice() {
        return price;
    }

    @JsonProperty("price")
    public void setPrice(double price) {
        this.price = price;
    }

    @JsonProperty("amount")
    public double getAmount() {
        return amount;
    }

    @JsonProperty("amount")
    public void setAmount(double amount) {
        this.amount = amount;
    }

    @JsonProperty("executed_at")
    public Long getExecutedAt() {
        return executedAt;
    }

    @JsonProperty("executed_at")
    public void setExecutedAt(Long executedAt) {
        this.executedAt = executedAt;
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
