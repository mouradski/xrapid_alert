package space.xrapid.domain.bithumb;

import com.fasterxml.jackson.annotation.*;

import java.util.HashMap;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "transaction_date",
        "type",
        "units_traded",
        "price",
        "total"
})
public class Datum {

    @JsonProperty("transaction_date")
    private String transactionDate;
    @JsonProperty("type")
    private String type;
    @JsonProperty("units_traded")
    private double unitsTraded;
    @JsonProperty("price")
    private double price;
    @JsonProperty("total")
    private double total;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("transaction_date")
    public String getTransactionDate() {
        return transactionDate;
    }

    @JsonProperty("transaction_date")
    public void setTransactionDate(String transactionDate) {
        this.transactionDate = transactionDate;
    }

    @JsonProperty("type")
    public String getType() {
        return type;
    }

    @JsonProperty("type")
    public void setType(String type) {
        this.type = type;
    }

    @JsonProperty("units_traded")
    public double getUnitsTraded() {
        return unitsTraded;
    }

    @JsonProperty("units_traded")
    public void setUnitsTraded(double unitsTraded) {
        this.unitsTraded = unitsTraded;
    }

    @JsonProperty("price")
    public double getPrice() {
        return price;
    }

    @JsonProperty("price")
    public void setPrice(double price) {
        this.price = price;
    }

    @JsonProperty("total")
    public double getTotal() {
        return total;
    }

    @JsonProperty("total")
    public void setTotal(double total) {
        this.total = total;
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