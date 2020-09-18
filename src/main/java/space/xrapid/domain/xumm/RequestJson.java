package space.xrapid.domain.xumm;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.util.HashMap;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "TransactionType",
    "Destination",
    "Fee",
    "Amount"
})
public class RequestJson {

  @JsonProperty("TransactionType")
  private String transactionType;
  @JsonProperty("Destination")
  private String destination;
  @JsonProperty("Fee")
  private String fee;
  @JsonProperty("Amount")
  private Amount amount;
  @JsonIgnore
  private Map<String, Object> additionalProperties = new HashMap<String, Object>();

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

  @JsonProperty("Fee")
  public String getFee() {
    return fee;
  }

  @JsonProperty("Fee")
  public void setFee(String fee) {
    this.fee = fee;
  }

  @JsonProperty("Amount")
  public Amount getAmount() {
    return amount;
  }

  @JsonProperty("Amount")
  public void setAmount(Amount amount) {
    this.amount = amount;
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
