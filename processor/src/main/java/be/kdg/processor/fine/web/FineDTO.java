package be.kdg.processor.fine.web;

import be.kdg.processor.camera.message.CameraMessage;
import be.kdg.processor.fine.FineType;
import be.kdg.processor.vehicle.web.VehicleDTO;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class FineDTO {
    private Integer id;
    private FineType type;
    @NotNull(message = "Price is required.")
    private double price;
    private double actualSpeed;
    private double maxSpeed;
    private int euroNorm;
    private int actualNorm;
    private String comment;
    private LocalDateTime creationDate;
    private boolean approved;
    private int messageSize;
    @JsonIgnore
    private List<CameraMessage> cameraMessages = new ArrayList<>();
    private VehicleDTO vehicle;

    public FineDTO(Integer id, FineType type, double price, int euroNorm, int actualNorm) {
        this.id = id;
        this.type = type;
        this.price = price;
        this.euroNorm = euroNorm;
        this.actualNorm = actualNorm;
    }
}
