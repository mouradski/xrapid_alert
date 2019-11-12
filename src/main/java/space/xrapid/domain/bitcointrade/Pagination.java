package space.xrapid.domain.bitcointrade;

import com.fasterxml.jackson.annotation.*;

import java.util.HashMap;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
"current_page",
"page_size",
"registers_count",
"total_pages"
})
public class Pagination {

@JsonProperty("current_page")
private Integer currentPage;
@JsonProperty("page_size")
private Integer pageSize;
@JsonProperty("registers_count")
private Integer registersCount;
@JsonProperty("total_pages")
private Integer totalPages;
@JsonIgnore
private Map<String, Object> additionalProperties = new HashMap<String, Object>();

@JsonProperty("current_page")
public Integer getCurrentPage() {
return currentPage;
}

@JsonProperty("current_page")
public void setCurrentPage(Integer currentPage) {
this.currentPage = currentPage;
}

@JsonProperty("page_size")
public Integer getPageSize() {
return pageSize;
}

@JsonProperty("page_size")
public void setPageSize(Integer pageSize) {
this.pageSize = pageSize;
}

@JsonProperty("registers_count")
public Integer getRegistersCount() {
return registersCount;
}

@JsonProperty("registers_count")
public void setRegistersCount(Integer registersCount) {
this.registersCount = registersCount;
}

@JsonProperty("total_pages")
public Integer getTotalPages() {
return totalPages;
}

@JsonProperty("total_pages")
public void setTotalPages(Integer totalPages) {
this.totalPages = totalPages;
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