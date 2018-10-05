package be.kdg.processor.camera.couple;

import be.kdg.processor.camera.Camera;
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
public class CameraCouple {
    @Id
    @GeneratedValue
    private int id;

    @OneToMany(targetEntity = Camera.class, cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "cameraCouple")
    private List<Camera> cameras = new ArrayList<>();

    @Column
    private int maxSpeed;

    @Column
    private int distance;

    public CameraCouple(int maxSpeed, int distance) {
        this.maxSpeed = maxSpeed;
        this.distance = distance;
    }

    public void addCamera(Camera camera) {
        cameras.add(camera);
        camera.setCameraCouple(this);
    }

    public void removeCamera(Camera camera) {
        cameras.remove(camera);
        camera.setCameraCouple(null);
    }
}

