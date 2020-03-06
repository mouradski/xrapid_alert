package space.xrapid.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Builder
@Entity(name = "DESTINATION_TAG")
public class DestinationTagRepeat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private long repeat;
    private String sourceAddress;
    private String destinationAddress;
    @Enumerated(EnumType.STRING)
    private Exchange source;
    @Enumerated(EnumType.STRING)
    private Exchange destination;
    private Long destinationTag;
    private double sum;
}
