package be.kdg.processor.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CameraMessage {
    @Id
    @GeneratedValue
    private Long id;

    @Column
    private int cameraId;

    @Column
    private String licensePlate;

    @Column(name = "`timestamp`")
    private LocalDateTime timestamp;

    private int delay;

    @Override
    public String toString() {
        return String.format("Camera %d spotted: %s at %s", cameraId, licensePlate,
                timestamp.format(DateTimeFormatter.ofPattern("dd-MM-YYYY HH:mm:ss")));
    }
}