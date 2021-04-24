package space.xrapid.domain.exchange.liquid;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
public class Trades {
    @JsonProperty("current_page")
    private Integer currentPage;
    @JsonProperty("total_pages")
    private Integer totalPages;
    @JsonProperty("models")
    private List<Trade> trades = null;
}