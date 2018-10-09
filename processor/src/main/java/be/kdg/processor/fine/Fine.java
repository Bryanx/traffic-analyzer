package be.kdg.processor.fine;

import be.kdg.processor.camera.message.CameraMessage;
import be.kdg.processor.vehicle.Vehicle;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Data
@Entity
public class Fine {
    @Id
    @GeneratedValue
    private Integer id;

    @Enumerated(EnumType.STRING)
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

    @Column
    private LocalDateTime creationDate;

    @Column
    private boolean approved;

    @LazyCollection(LazyCollectionOption.FALSE)
    @OneToMany(targetEntity = CameraMessage.class, cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "fine")
    private List<CameraMessage> cameraMessages = new ArrayList<>();

    @ManyToOne(targetEntity = Vehicle.class)
    @JoinColumn(name = "vehicle_id")
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
        this.creationDate = LocalDateTime.now();
    }

    public Fine(FineType type, double price, double actualSpeed, double maxSpeed) {
        this.type = type;
        this.price = price;
        this.actualSpeed = actualSpeed;
        this.maxSpeed = maxSpeed;
        this.creationDate = LocalDateTime.now();
    }
}
