package be.kdg.processor.camera;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class CameraLocationDTO {
    @JsonProperty("lat")
    private Double latitude;
    @JsonProperty("long")
    private Double longitude;
}