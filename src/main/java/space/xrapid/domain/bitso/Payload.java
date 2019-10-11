package space.xrapid.domain.bitso;

import com.fasterxml.jackson.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
"asks",
"bids",
"updated_at",
"sequence"
})
public class Payload {

@JsonProperty("asks")
private List<Ask> asks = null;
@JsonProperty("bids")
private List<Bid> bids = null;
@JsonProperty("updated_at")
private String updatedAt;
@JsonProperty("sequence")
private String sequence;
@JsonIgnore
private Map<String, Object> additionalProperties = new HashMap<String, Object>();

@JsonProperty("asks")
public List<Ask> getAsks() {
return asks;
}

@JsonProperty("asks")
public void setAsks(List<Ask> asks) {
this.asks = asks;
}

@JsonProperty("bids")
public List<Bid> getBids() {
return bids;
}

@JsonProperty("bids")
public void setBids(List<Bid> bids) {
this.bids = bids;
}

@JsonProperty("updated_at")
public String getUpdatedAt() {
return updatedAt;
}

@JsonProperty("updated_at")
public void setUpdatedAt(String updatedAt) {
this.updatedAt = updatedAt;
}

@JsonProperty("sequence")
public String getSequence() {
return sequence;
}

@JsonProperty("sequence")
public void setSequence(String sequence) {
this.sequence = sequence;
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
