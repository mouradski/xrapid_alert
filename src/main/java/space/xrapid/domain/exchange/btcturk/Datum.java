package space.xrapid.domain.exchange.btcturk;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
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
}
