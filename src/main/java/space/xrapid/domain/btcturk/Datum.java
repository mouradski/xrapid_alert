package space.xrapid.domain.btcturk;

import com.fasterxml.jackson.annotation.*;

import java.util.HashMap;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "pair",
        "pairNormalized",
        "numerator",
        "denominator",
        "date",
        "tid",
        "price",
        "amount",
        "side"
})
public class Datum {

    @JsonProperty("pair")
    private String pair;
    @JsonProperty("pairNormalized")
    private String pairNormalized;
    @JsonProperty("numerator")
    private String numerator;
    @JsonProperty("denominator")
    private String denominator;
    @JsonProperty("date")
    private Long date;
    @JsonProperty("tid")
    private String tid;
    @JsonProperty("price")
    private Double price;
    @JsonProperty("amount")
    private Double amount;
    @JsonProperty("side")
    private String side;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("pair")
    public String getPair() {
        return pair;
    }

    @JsonProperty("pair")
    public void setPair(String pair) {
        this.pair = pair;
    }

    @JsonProperty("pairNormalized")
    public String getPairNormalized() {
        return pairNormalized;
    }

    @JsonProperty("pairNormalized")
    public void setPairNormalized(String pairNormalized) {
        this.pairNormalized = pairNormalized;
    }

    @JsonProperty("numerator")
    public String getNumerator() {
        return numerator;
    }

    @JsonProperty("numerator")
    public void setNumerator(String numerator) {
        this.numerator = numerator;
    }

    @JsonProperty("denominator")
    public String getDenominator() {
        return denominator;
    }

    @JsonProperty("denominator")
    public void setDenominator(String denominator) {
        this.denominator = denominator;
    }

    @JsonProperty("date")
    public Long getDate() {
        return date;
    }

    @JsonProperty("date")
    public void setDate(Long date) {
        this.date = date;
    }

    @JsonProperty("tid")
    public String getTid() {
        return tid;
    }

    @JsonProperty("tid")
    public void setTid(String tid) {
        this.tid = tid;
    }

    @JsonProperty("price")
    public Double getPrice() {
        return price;
    }

    @JsonProperty("price")
    public void setPrice(Double price) {
        this.price = price;
    }

    @JsonProperty("amount")
    public Double getAmount() {
        return amount;
    }

    @JsonProperty("amount")
    public void setAmount(Double amount) {
        this.amount = amount;
    }

    @JsonProperty("side")
    public String getSide() {
        return side;
    }

    @JsonProperty("side")
    public void setSide(String side) {
        this.side = side;
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
