package be.kdg.processor.camera.message;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CameraMessageDTO {
    private int cameraId;
    private String licensePlate;
    private LocalDateTime timestamp;
    private int delay;
}
