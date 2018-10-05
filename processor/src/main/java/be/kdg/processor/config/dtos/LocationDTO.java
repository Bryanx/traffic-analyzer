package be.kdg.processor.config.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class LocationDTO {
    @JsonProperty("lat")
    private Double latitude;
    @JsonProperty("long")
    private Double longitude;
}