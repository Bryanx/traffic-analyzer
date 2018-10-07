package be.kdg.processor.camera.segment;

import be.kdg.processor.camera.Camera;
import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class Segment {

    @Id
    @GeneratedValue
    private int id;

    @OneToMany(targetEntity = Camera.class, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Camera> cameras = new ArrayList<>();

    private int connectedCameraId;
    private int distance;
    private int speedLimit;

    public void addCamera(Camera camera) {
        cameras.add(camera);
        camera.setSegment(this);
    }

    public void removeCamera(Camera camera) {
        cameras.remove(camera);
        camera.setSegment(null);
    }
}