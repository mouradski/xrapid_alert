package space.xrapid.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class TradesGroup {
    private String id;
    private double sum;
}
