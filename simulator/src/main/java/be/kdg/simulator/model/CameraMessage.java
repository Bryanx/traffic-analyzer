package be.kdg.simulator.model;

import lombok.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Data
@AllArgsConstructor
public class CameraMessage {
    @NonNull
    private int cameraId;
    @NonNull
    private String licensePlate;
    private LocalDateTime timestamp;
    private int delay;

    public CameraMessage(int cameraId, String licensePlate, LocalDateTime timestamp) {
        this.cameraId = cameraId;
        this.licensePlate = licensePlate;
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return String.format("camera %d spotted: %s at %s", cameraId, licensePlate,
                timestamp.format(DateTimeFormatter.ofPattern("dd-MM-YYYY HH:mm:ss")));
    }
}