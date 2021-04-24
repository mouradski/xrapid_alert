package space.xrapid.domain.exchange.wazirx;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
public class Trade {
    @JsonProperty("id")
    private Integer id;
    @JsonProperty("price")
    private double price;
    @JsonProperty("volume")
    private double volume;
    @JsonProperty("funds")
    private double funds;
    @JsonProperty("market")
    private String market;
    @JsonProperty("created_at")
    private String createdAt;
    @JsonProperty("side")
    private String side;
}