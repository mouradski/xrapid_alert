package space.xrapid.domain.bitso;

import com.fasterxml.jackson.annotation.*;
import lombok.ToString;

import java.util.HashMap;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "book",
        "created_at",
        "amount",
        "maker_side",
        "price",
        "tid"
})
@ToString
public class Trade {

    @JsonProperty("book")
    private String book;
    @JsonProperty("created_at")
    private String createdAt;
    @JsonProperty("amount")
    private String amount;
    @JsonProperty("maker_side")
    private String makerSide;
    @JsonProperty("price")
    private String price;
    @JsonProperty("tid")
    private Integer tid;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("book")
    public String getBook() {
        return book;
    }

    @JsonProperty("book")
    public void setBook(String book) {
        this.book = book;
    }

    @JsonProperty("created_at")
    public String getCreatedAt() {
        return createdAt;
    }

    @JsonProperty("created_at")
    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    @JsonProperty("amount")
    public String getAmount() {
        return amount;
    }

    @JsonProperty("amount")
    public void setAmount(String amount) {
        this.amount = amount;
    }

    @JsonProperty("maker_side")
    public String getMakerSide() {
        return makerSide;
    }

    @JsonProperty("maker_side")
    public void setMakerSide(String makerSide) {
        this.makerSide = makerSide;
    }

    @JsonProperty("price")
    public String getPrice() {
        return price;
    }

    @JsonProperty("price")
    public void setPrice(String price) {
        this.price = price;
    }

    @JsonProperty("tid")
    public Integer getTid() {
        return tid;
    }

    @JsonProperty("tid")
    public void setTid(Integer tid) {
        this.tid = tid;
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
