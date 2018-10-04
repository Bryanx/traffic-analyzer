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
public class Camera {
    @Id
    private int id;

    @Column
    private double latitude;

    @Column
    private double longitude;

    @OneToMany(targetEntity = CameraMessage.class, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CameraMessage> cameraMessages = new ArrayList<>();

    @ManyToOne(targetEntity = CameraCouple.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "cameracouple_id")
    private CameraCouple cameraCouple;

    public Camera(JsonObject input) {
        this.id = input.get("cameraId").getAsInt();
        if (input.getAsJsonObject("location") != null) {
            this.latitude = input.getAsJsonObject("location").get("lat").getAsDouble();
            this.longitude = input.getAsJsonObject("location").get("long").getAsDouble();
        }
    }

    public Camera(int cameraId) {
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
