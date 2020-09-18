package space.xrapid.domain.ripple;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "amount",
    "delivered_amount",
    "destination_balance_changes",
    "source_balance_changes",
    "tx_index",
    "currency",
    "destination",
    "destination_tag",
    "executed_time",
    "ledger_index",
    "source",
    "source_currency",
    "tx_hash",
    "transaction_cost"
})
public class Payment {

  @JsonProperty("amount")
  private double amount;
  @JsonProperty("delivered_amount")
  private double deliveredAmount;
  @JsonProperty("destination_balance_changes")
  private List<DestinationBalanceChange> destinationBalanceChanges = null;
  @JsonProperty("source_balance_changes")
  private List<SourceBalanceChange> sourceBalanceChanges = null;
  @JsonProperty("tx_index")
  private Integer txIndex;
  @JsonProperty("currency")
  private String currency;
  @JsonProperty("destination")
  private String destination;
  @JsonProperty("destination_tag")
  private Long destinationTag;
  @JsonProperty("executed_time")
  private String executedTime;
  @JsonProperty("ledger_index")
  private Integer ledgerIndex;
  @JsonProperty("source")
  private String source;
  @JsonProperty("source_currency")
  private String sourceCurrency;
  @JsonProperty("tx_hash")
  private String txHash;
  @JsonProperty("transaction_cost")
  private String transactionCost;
  @JsonIgnore
  private Map<String, Object> additionalProperties = new HashMap<String, Object>();

  @JsonProperty("amount")
  public double getAmount() {
    return amount;
  }

  @JsonProperty("amount")
  public void setAmount(double amount) {
    this.amount = amount;
  }

  @JsonProperty("delivered_amount")
  public double getDeliveredAmount() {
    return deliveredAmount;
  }

  @JsonProperty("delivered_amount")
  public void setDeliveredAmount(double deliveredAmount) {
    this.deliveredAmount = deliveredAmount;
  }

  @JsonProperty("destination_balance_changes")
  public List<DestinationBalanceChange> getDestinationBalanceChanges() {
    return destinationBalanceChanges;
  }

  @JsonProperty("destination_balance_changes")
  public void setDestinationBalanceChanges(
      List<DestinationBalanceChange> destinationBalanceChanges) {
    this.destinationBalanceChanges = destinationBalanceChanges;
  }

  @JsonProperty("source_balance_changes")
  public List<SourceBalanceChange> getSourceBalanceChanges() {
    return sourceBalanceChanges;
  }

  @JsonProperty("source_balance_changes")
  public void setSourceBalanceChanges(List<SourceBalanceChange> sourceBalanceChanges) {
    this.sourceBalanceChanges = sourceBalanceChanges;
  }

  @JsonProperty("tx_index")
  public Integer getTxIndex() {
    return txIndex;
  }

  @JsonProperty("tx_index")
  public void setTxIndex(Integer txIndex) {
    this.txIndex = txIndex;
  }

  @JsonProperty("currency")
  public String getCurrency() {
    return currency;
  }

  @JsonProperty("currency")
  public void setCurrency(String currency) {
    this.currency = currency;
  }

  @JsonProperty("destination")
  public String getDestination() {
    return destination;
  }

  @JsonProperty("destination")
  public void setDestination(String destination) {
    this.destination = destination;
  }

  @JsonProperty("destination_tag")
  public Long getDestinationTag() {
    return destinationTag;
  }

  @JsonProperty("destination_tag")
  public void setDestinationTag(Long destinationTag) {
    this.destinationTag = destinationTag;
  }

  @JsonProperty("executed_time")
  public String getExecutedTime() {
    return executedTime;
  }

  @JsonProperty("executed_time")
  public void setExecutedTime(String executedTime) {
    this.executedTime = executedTime;
  }

  @JsonProperty("ledger_index")
  public Integer getLedgerIndex() {
    return ledgerIndex;
  }

  @JsonProperty("ledger_index")
  public void setLedgerIndex(Integer ledgerIndex) {
    this.ledgerIndex = ledgerIndex;
  }

  @JsonProperty("source")
  public String getSource() {
    return source;
  }

  @JsonProperty("source")
  public void setSource(String source) {
    this.source = source;
  }

  @JsonProperty("source_currency")
  public String getSourceCurrency() {
    return sourceCurrency;
  }

  @JsonProperty("source_currency")
  public void setSourceCurrency(String sourceCurrency) {
    this.sourceCurrency = sourceCurrency;
  }

  @JsonProperty("tx_hash")
  public String getTxHash() {
    return txHash;
  }

  @JsonProperty("tx_hash")
  public void setTxHash(String txHash) {
    this.txHash = txHash;
  }

  @JsonProperty("transaction_cost")
  public String getTransactionCost() {
    return transactionCost;
  }

  @JsonProperty("transaction_cost")
  public void setTransactionCost(String transactionCost) {
    this.transactionCost = transactionCost;
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
