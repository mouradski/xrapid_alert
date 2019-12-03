package space.xrapid.domain.bx;

import com.fasterxml.jackson.annotation.*;

import java.util.HashMap;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "trade_id",
        "rate",
        "amount",
        "trade_date",
        "order_id",
        "trade_type",
        "reference_id",
        "seconds"
})
public class Trade {

    @JsonProperty("trade_id")
    private String tradeId;
    @JsonProperty("rate")
    private String rate;
    @JsonProperty("amount")
    private String amount;
    @JsonProperty("trade_date")
    private String tradeDate;
    @JsonProperty("order_id")
    private String orderId;
    @JsonProperty("trade_type")
    private String tradeType;
    @JsonProperty("reference_id")
    private String referenceId;
    @JsonProperty("seconds")
    private Integer seconds;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("trade_id")
    public String getTradeId() {
        return tradeId;
    }

    @JsonProperty("trade_id")
    public void setTradeId(String tradeId) {
        this.tradeId = tradeId;
    }

    @JsonProperty("rate")
    public String getRate() {
        return rate;
    }

    @JsonProperty("rate")
    public void setRate(String rate) {
        this.rate = rate;
    }

    @JsonProperty("amount")
    public String getAmount() {
        return amount;
    }

    @JsonProperty("amount")
    public void setAmount(String amount) {
        this.amount = amount;
    }

    @JsonProperty("trade_date")
    public String getTradeDate() {
        return tradeDate;
    }

    @JsonProperty("trade_date")
    public void setTradeDate(String tradeDate) {
        this.tradeDate = tradeDate;
    }

    @JsonProperty("order_id")
    public String getOrderId() {
        return orderId;
    }

    @JsonProperty("order_id")
    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    @JsonProperty("trade_type")
    public String getTradeType() {
        return tradeType;
    }

    @JsonProperty("trade_type")
    public void setTradeType(String tradeType) {
        this.tradeType = tradeType;
    }

    @JsonProperty("reference_id")
    public String getReferenceId() {
        return referenceId;
    }

    @JsonProperty("reference_id")
    public void setReferenceId(String referenceId) {
        this.referenceId = referenceId;
    }

    @JsonProperty("seconds")
    public Integer getSeconds() {
        return seconds;
    }

    @JsonProperty("seconds")
    public void setSeconds(Integer seconds) {
        this.seconds = seconds;
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
