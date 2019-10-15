package space.xrapid.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;

@Getter
@Setter
@Builder
public class XrpTrade {
    private OffsetDateTime dateTime;
    private Long timestamp;
    private Double amount;
    private Exchange target;
    private String orderId;
    private double rate;
}
