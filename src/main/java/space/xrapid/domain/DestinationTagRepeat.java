package space.xrapid.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
  private String source;
  private String destination;
  private Long destinationTag;
  private double sum;

  @Override
  public String toString() {
    return new StringBuilder(source != null ? source.toString() : "").append(";")
        .append(sourceAddress).append(";")
        .append(destination != null ? destination.toString() : "").append(";")
        .append(destinationAddress).append(";")
        .append(destinationTag == null ? "0" : destinationTag).append(";").append(repeated)
        .toString();
  }
}
