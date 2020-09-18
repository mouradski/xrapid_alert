package space.xrapid.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.time.OffsetDateTime;
import java.util.Map;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@JsonPropertyOrder({
    "allTimeFrom",
    "todayVolume",
    "totalVolume",
    "todayVolumePerCorridor",
    "side",
    "dailyAth",
    "athPerCorridor",
    "volumePerCorridor"
})
public class GlobalStats {

  @JsonProperty("todayVolumePerCorridor")
  private Map<String, Double> todayVolumePerCorridor;

  @JsonProperty("todayVolume")
  private double todayVolume;
  @JsonProperty("totalVolume")
  private double totalVolume;
  @JsonProperty("dailyAth")
  private double dailyAth;
  @JsonProperty("athPerCorridor")
  private Map<String, Double> athPerCorridor;
  @JsonProperty("volumePerCorridor")
  private Map<String, Map<String, Double>> volumePerCorridor;
  @JsonProperty("allTimeFrom")
  private OffsetDateTime allTimeFrom;
}
