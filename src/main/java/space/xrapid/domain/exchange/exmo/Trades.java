package space.xrapid.domain.exchange.exmo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
public class Trades {
    @JsonProperty("XRP_UAH")
    private List<XRPUAH> xRPUAH = null;
}
