package space.xrapid.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class XrpTrade {
    private Long timestamp;
    private Double amount;
    private Exchange target;
    private String orderId;
    private double rate;
}
