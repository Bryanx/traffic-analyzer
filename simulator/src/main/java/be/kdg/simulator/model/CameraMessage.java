package be.kdg.simulator.model;

import be.kdg.simulator.config.converters.LocalDateTimeAdapter;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Data
@AllArgsConstructor
@NoArgsConstructor
@XmlRootElement(name="CameraMessage")
public class CameraMessage {
    private int cameraId;
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

    @XmlJavaTypeAdapter(LocalDateTimeAdapter.class)
    public LocalDateTime getTimestamp() {
        return timestamp;
    }


}