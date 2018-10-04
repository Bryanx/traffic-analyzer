package be.kdg.processor.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CameraMessage {
    @Id
    @GeneratedValue
    private int id;

    @ManyToOne(targetEntity = Camera.class)
    @JoinColumn(name = "camera_id")
    private Camera camera;

    @Column
    private String licensePlate;

    @Column(name = "`timestamp`")
    private LocalDateTime timestamp;

    @ManyToOne(targetEntity = Fine.class)
    @JoinColumn(name = "fine_id")
    private Fine fine;

    public CameraMessage(String licensePlate, LocalDateTime timestamp) {
        this.licensePlate = licensePlate;
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        if (camera == null) {
            return String.format("CameraMessage: %s at %s", licensePlate,
                    timestamp.format(DateTimeFormatter.ofPattern("dd-MM-YYYY HH:mm:ss")));
        }
        return String.format("Camera %d spotted: %s at %s", camera.getId(), licensePlate,
                timestamp.format(DateTimeFormatter.ofPattern("dd-MM-YYYY HH:mm:ss")));
    }
}