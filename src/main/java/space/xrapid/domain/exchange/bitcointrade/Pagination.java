package space.xrapid.domain.exchange.bitcointrade;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
public class Pagination {

    @JsonProperty("current_page")
    private Integer currentPage;
    @JsonProperty("page_size")
    private Integer pageSize;
    @JsonProperty("registers_count")
    private Integer registersCount;
    @JsonProperty("total_pages")
    private Integer totalPages;
}