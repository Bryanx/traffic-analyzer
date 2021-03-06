package be.kdg.processor.camera;

import be.kdg.processor.camera.location.Location;
import be.kdg.processor.camera.message.CameraMessage;
import be.kdg.processor.camera.segment.Segment;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Camera {
    @Id
    private int cameraId;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Location location;

    private int euroNorm;

    @OneToMany(targetEntity = CameraMessage.class, cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "camera")
    private List<CameraMessage> cameraMessages = new ArrayList<>();

    @ManyToOne(targetEntity = Segment.class)
    @JoinColumn(name = "segment_id")
    private Segment segment;

    public Camera(int cameraId) {
        this.cameraId = cameraId;
    }

    public void addCameraMessage(CameraMessage msg) {
        cameraMessages.add(msg);
        msg.setCamera(this);
    }

    public void removeCameraMessage(CameraMessage msg) {
        cameraMessages.remove(msg);
        msg.setCamera(null);
    }

    @Override
    public String toString() {
        return "Camera{" +
                "cameraId=" + cameraId +
                '}';
    }
}
