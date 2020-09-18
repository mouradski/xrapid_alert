package space.xrapid.domain;

import java.time.OffsetDateTime;
import java.util.Map;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class Stats {

  Map<String, Double> topVolumes;
  private double todayVolume;
  private double allTimeVolume;
  private double athDaylyVolume;
  private long averageTimeBetweetTransactions;
  private OffsetDateTime allTimeFrom;
  private double[] last5DaysOdlVolume;
  private String[] days;
}
