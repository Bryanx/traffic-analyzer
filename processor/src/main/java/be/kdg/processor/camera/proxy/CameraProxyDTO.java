package be.kdg.processor.camera.proxy;

import be.kdg.processor.camera.CameraLocationDTO;
import be.kdg.processor.camera.couple.CameraCoupleDTO;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
@Data
public class CameraProxyDTO {
    private Integer cameraId;
    private CameraLocationDTO location;
    private CameraCoupleDTO segment;
    private Integer euroNorm;

    public boolean isInSameSegment(CameraProxyDTO other) {
        if (other.segment == null) return false;
        return other.segment.getConnectedCameraId().equals(this.cameraId);
    }
}

