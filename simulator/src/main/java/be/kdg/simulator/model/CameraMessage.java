package be.kdg.simulator.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Setter;
import lombok.AccessLevel;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Data
@Setter(AccessLevel.NONE)
@AllArgsConstructor
public class CameraMessage {
    private int id;
    private String licensePlate;
    private LocalDateTime timestamp;

    @Override
    public String toString() {
        return String.format("Camera Message %d %s %s", id, licensePlate,
                timestamp.format(DateTimeFormatter.ofPattern("dd-MM-YYYY HH:mm:ss")));
    }
}