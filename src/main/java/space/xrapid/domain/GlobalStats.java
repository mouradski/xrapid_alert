package space.xrapid.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;
import java.util.Map;

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
        "athsPerCorridor",
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
    @JsonProperty("athsPerCorridor")
    private Map<String, Double> athsPerCorridor;
    @JsonProperty("volumePerCorridor")
    private Map<String, Map<String, Double>> volumePerCorridor;
    @JsonProperty("allTimeFrom")
    private OffsetDateTime allTimeFrom;
}
