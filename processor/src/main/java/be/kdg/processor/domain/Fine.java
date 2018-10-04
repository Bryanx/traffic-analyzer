package be.kdg.processor.domain;

import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

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

    @OneToMany(targetEntity = CameraMessage.class, cascade = CascadeType.DETACH, orphanRemoval = false)
    private List<CameraMessage> cameraMessages = new ArrayList<>();

    public Fine(FineType type, double price, double actualSpeed, double maxSpeed, List<CameraMessage> cameraMessages) {
        this.type = type;
        this.price = price;
        this.actualSpeed = actualSpeed;
        this.maxSpeed = maxSpeed;
        this.cameraMessages = cameraMessages;
    }
}
