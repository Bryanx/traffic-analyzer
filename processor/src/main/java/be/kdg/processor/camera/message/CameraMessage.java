package be.kdg.processor.camera.message;

import be.kdg.processor.camera.Camera;
import be.kdg.processor.fine.Fine;
import be.kdg.processor.shared.converters.LocalDateTimeAdapter;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@XmlRootElement(name = "CameraMessage")
public class CameraMessage {

    @Id
    @GeneratedValue
    private int id;

    @ManyToOne(targetEntity = Camera.class)
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

    private int cameraId;

    @XmlJavaTypeAdapter(LocalDateTimeAdapter.class)
    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    @Override
    public String toString() {
        if (camera == null) {
            return String.format("CameraMessage: %s at %s", licensePlate,
                    timestamp.format(DateTimeFormatter.ofPattern("dd-MM-YYYY HH:mm:ss")));
        }
        return String.format("camera %d spotted: %s at %s", camera.getCameraId(), licensePlate,
                timestamp.format(DateTimeFormatter.ofPattern("dd-MM-YYYY HH:mm:ss")));
    }
}