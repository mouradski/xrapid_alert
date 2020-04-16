package space.xrapid.domain;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Data
@Entity(name = "API_KEYS")
public class ApiKey {

    private Date expiration;

    @Id
    private String key;

    private long lastUse;

    private boolean master;
}
