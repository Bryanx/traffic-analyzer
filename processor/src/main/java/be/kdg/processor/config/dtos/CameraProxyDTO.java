package be.kdg.processor.config.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
@Data
public class CameraProxyDTO {
    private Integer cameraId;
    private LocationDTO location;
    private SegmentDTO segment;
    private Integer euroNorm;

    public boolean isInSameSegment(CameraProxyDTO other) {
        if (other.segment.getConnectedCameraId() == null) return false;
        return other.segment.getConnectedCameraId().equals(this.cameraId);
    }
}

