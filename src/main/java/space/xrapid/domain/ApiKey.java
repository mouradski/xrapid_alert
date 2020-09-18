package space.xrapid.domain;

import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

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

  private boolean ban;
}
