package be.kdg.processor.camera;

import be.kdg.processor.camera.couple.CameraCouple;
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

    @OneToOne
    private Location location;

    @OneToOne
    private Segment segment;

    @Column
    private int euroNorm;

    @OneToMany(targetEntity = CameraMessage.class, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CameraMessage> cameraMessages = new ArrayList<>();

    @ManyToOne(targetEntity = CameraCouple.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "cameracouple_id")
    private CameraCouple cameraCouple;

    public void addCameraMessage(CameraMessage msg) {
        cameraMessages.add(msg);
        msg.setCamera(this);
    }

    public void removeCameraMessage(CameraMessage msg) {
        cameraMessages.remove(msg);
        msg.setCamera(null);
    }
}
