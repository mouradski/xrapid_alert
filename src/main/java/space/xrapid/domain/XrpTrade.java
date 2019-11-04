package space.xrapid.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class XrpTrade {
    private double amount;
    private Exchange target;
}
