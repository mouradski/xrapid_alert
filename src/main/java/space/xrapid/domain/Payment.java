package space.xrapid.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class Payment {
    private Long timestamp;
    private Double amount;
    private String transactionHash;
}
