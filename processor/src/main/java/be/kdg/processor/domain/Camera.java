package be.kdg.processor.domain;

import com.google.gson.JsonObject;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Camera {
    @Id
    private Long id;

    @Column
    private double latitude;

    @Column
    private double longitude;

    @OneToMany(targetEntity = CameraMessage.class, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CameraMessage> cameraMessages = new ArrayList<>();

    @ManyToOne(targetEntity = CameraCouple.class)
    @JoinColumn(name = "cameracouple_id")
    private CameraCouple cameraCouple;

    public Camera(JsonObject input) {
        this.id = input.get("cameraId").getAsLong();
        if (input.getAsJsonObject("location") != null) {
            this.latitude = input.getAsJsonObject("location").get("lat").getAsDouble();
            this.longitude = input.getAsJsonObject("location").get("long").getAsDouble();
        }
    }

    public Camera(long cameraId) {
        this.id = cameraId;
    }

    public void addCameraMessage(CameraMessage msg) {
        cameraMessages.add(msg);
        msg.setCamera(this);
    }

    public void removeCameraMessage(CameraMessage msg) {
        cameraMessages.remove(msg);
        msg.setCamera(null);
    }
}
