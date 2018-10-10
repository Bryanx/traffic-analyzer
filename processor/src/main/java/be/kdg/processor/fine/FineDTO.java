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
    double actualSpeed;
    double maxSpeed;
    int euroNorm;
    int actualNorm;
    private LocalDateTime creationDate;
    private boolean approved;
    private List<CameraMessage> cameraMessages = new ArrayList<>();
    private Vehicle vehicle;
}
