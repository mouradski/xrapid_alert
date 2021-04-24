package space.xrapid.domain.ripple;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
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
}
