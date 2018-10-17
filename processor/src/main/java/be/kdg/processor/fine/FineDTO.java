package be.kdg.processor.fine;

import be.kdg.processor.camera.message.CameraMessage;
import be.kdg.processor.vehicle.Vehicle;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class FineDTO {
    private Integer id;
    private FineType type;
    private double price;
    private double actualSpeed;
    private double maxSpeed;
    private int euroNorm;
    private int actualNorm;
    private String comment;
    private LocalDateTime creationDate;
    private boolean approved;
    private List<CameraMessage> cameraMessages = new ArrayList<>();
    private Vehicle vehicle;

    public FineDTO(Integer id, FineType type, double price, int euroNorm, int actualNorm) {
        this.id = id;
        this.type = type;
        this.price = price;
        this.euroNorm = euroNorm;
        this.actualNorm = actualNorm;
    }
}
