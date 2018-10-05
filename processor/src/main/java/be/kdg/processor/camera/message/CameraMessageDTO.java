package be.kdg.processor.camera.message;

import be.kdg.processor.shared.converters.LocalDateTimeAdapter;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.time.LocalDateTime;

@Data
@XmlRootElement(name="CameraMessage")
@NoArgsConstructor
public class CameraMessageDTO {
    private int cameraId;
    private int delay;
    private String licensePlate;
    private LocalDateTime timestamp;

    @XmlJavaTypeAdapter(LocalDateTimeAdapter.class)
    public LocalDateTime getTimestamp() {
        return timestamp;
    }
}
