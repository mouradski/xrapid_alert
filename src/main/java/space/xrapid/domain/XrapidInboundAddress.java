package space.xrapid.domain;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class XrapidInboundAddress {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String address;
    private long tag;

    @Enumerated(EnumType.STRING)
    private Currency sourceFiat;

    private int recurrence;
}
