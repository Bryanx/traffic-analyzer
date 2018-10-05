package be.kdg.processor.camera.couple;

import lombok.Data;

@Data
public class CameraCoupleDTO {
    private Integer connectedCameraId;
    private Integer distance;
    private Integer speedLimit;
}