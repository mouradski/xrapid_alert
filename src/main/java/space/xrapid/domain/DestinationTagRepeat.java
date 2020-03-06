package space.xrapid.domain;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "DESTINATION_TAG")
public class DestinationTagRepeat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Long repeated;
    private String sourceAddress;
    private String destinationAddress;
    @Enumerated(EnumType.STRING)
    private Exchange source;
    @Enumerated(EnumType.STRING)
    private Exchange destination;
    private Long destinationTag;
    private double sum;

    @Override
    public String toString() {
        return new StringBuilder(source != null ? source.toString() : "").append(";").append(sourceAddress).append(";")
                .append(destination != null ? destination.toString() : "").append(";").append(destinationAddress).append(";")
                .append(destinationTag == null ? "0" : destinationTag).append(";").append(repeated).toString();
    }
}
