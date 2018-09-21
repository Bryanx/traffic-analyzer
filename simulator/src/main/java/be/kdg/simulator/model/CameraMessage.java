package be.kdg.simulator.model;

import lombok.Data;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Data
public class CameraMessage {
    private int id;
    private String licensePlate;
    private LocalDateTime timestamp;

    public CameraMessage(int id, String licensePlate, LocalDateTime timestamp) {
        this.id = id;
        this.licensePlate = licensePlate;
        this.timestamp = timestamp;
    }

    //TODO: Format time correctly.
    @Override
    public String toString() {
        return String.format("Camera Message %d %s %s", id, licensePlate,
                timestamp.format(DateTimeFormatter.ofPattern("dd-MM-YYYY HH:mm:ss")));
    }
}