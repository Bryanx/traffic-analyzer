package be.kdg.processor.fine;

import be.kdg.processor.camera.message.CameraMessage;
import be.kdg.processor.vehicle.Vehicle;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Data
@Entity
public class Fine {
    @Id
    @GeneratedValue
    private Integer id;

    @Column
    private FineType type;

    @Column
    private double price;

    @Column
    double actualSpeed;

    @Column
    double maxSpeed;

    @Column
    int euroNorm;

    @Column
    int actualNorm;

    @OneToMany(targetEntity = CameraMessage.class, cascade = CascadeType.DETACH, orphanRemoval = false)
    private List<CameraMessage> cameraMessages = new ArrayList<>();

    @ManyToOne(targetEntity = Vehicle.class)
    private Vehicle vehicle;

    public void addCameraMessage(CameraMessage msg) {
        cameraMessages.add(msg);
        msg.setFine(this);
    }

    public Fine(FineType type, double price, int euroNorm, int actualNorm) {
        this.type = type;
        this.price = price;
        this.euroNorm = euroNorm;
        this.actualNorm = actualNorm;
    }

    public Fine(FineType type, double price, double actualSpeed, double maxSpeed) {
        this.type = type;
        this.price = price;
        this.actualSpeed = actualSpeed;
        this.maxSpeed = maxSpeed;
    }
}
