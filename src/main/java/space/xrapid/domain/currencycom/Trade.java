package space.xrapid.domain.currencycom;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "a",
        "p",
        "q",
        "T",
        "m"
})
public class Trade {

    @JsonProperty("a")
    private String a;
    @JsonProperty("p")
    private double p;
    @JsonProperty("q")
    private double q;
    @JsonProperty("T")
    private long t;
    @JsonProperty("m")
    private Boolean m;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("a")
    public String getA() {
        return a;
    }

    @JsonProperty("a")
    public void setA(String a) {
        this.a = a;
    }

    @JsonProperty("p")
    public double getP() {
        return p;
    }

    @JsonProperty("p")
    public void setP(double p) {
        this.p = p;
    }

    @JsonProperty("q")
    public double getQ() {
        return q;
    }

    @JsonProperty("q")
    public void setQ(double q) {
        this.q = q;
    }

    @JsonProperty("T")
    public long getT() {
        return t;
    }

    @JsonProperty("T")
    public void setT(long t) {
        this.t = t;
    }

    @JsonProperty("m")
    public Boolean getM() {
        return m;
    }

    @JsonProperty("m")
    public void setM(Boolean m) {
        this.m = m;
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
