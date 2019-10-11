package space.xrapid.domain.bitso;

import com.fasterxml.jackson.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
"success",
"payment"
})
public class BitsoPayments {

@JsonProperty("success")
private Boolean success;
@JsonProperty("payload")
private List<Trade> payment = null;
@JsonIgnore
private Map<String, Object> additionalProperties = new HashMap<String, Object>();

@JsonProperty("success")
public Boolean getSuccess() {
return success;
}

@JsonProperty("success")
public void setSuccess(Boolean success) {
this.success = success;
}

@JsonProperty("payload")
public List<Trade> getPayment() {
return payment;
}

@JsonProperty("payment")
public void setPayment(List<Trade> payment) {
this.payment = payment;
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
