package be.kdg.processor.config.helpers;

import lombok.Data;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.time.LocalDateTime;

@Data
@XmlRootElement(name="CameraMessageDTO")
public class CameraMessageDTO {
    @XmlElement(name="cameraId")
    private int cameraId;
    @XmlElement(name="licensePlate")
    private String licensePlate;
    @XmlElement(name="timestamp")
    private LocalDateTime timestamp;
    @XmlElement(name="delay")
    private int delay;
}
