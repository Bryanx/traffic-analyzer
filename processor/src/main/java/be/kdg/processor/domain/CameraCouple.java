package be.kdg.processor.domain;

import com.google.gson.JsonObject;
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
    private Long id;

    @OneToMany(targetEntity = Camera.class, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Camera> cameras = new ArrayList<>();

    @Column
    private int maxSpeed;

    @Column
    private int distance;

    public CameraCouple(JsonObject input) {
        this.maxSpeed = input.get("speedLimit").getAsInt();
        this.distance = input.get("distance").getAsInt();
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

