package space.xrapid.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.OffsetDateTime;

@Getter
@Setter
@Builder
@ToString
public class Trade {
    private OffsetDateTime dateTime;
    private Long timestamp;
    private Double amount;
    private Exchange target;
    private String orderId;
    private double rate;
    private String side;


    public String getDateTimeAndOrderSide() {
        return new StringBuilder().append(this.getSide()).append(":").append(this.getDateTime()).toString();
    }
}
