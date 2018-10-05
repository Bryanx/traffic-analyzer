package be.kdg.processor.config.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class SegmentDTO {
    private Integer connectedCameraId;
    private Integer distance;
    private Integer speedLimit;
}