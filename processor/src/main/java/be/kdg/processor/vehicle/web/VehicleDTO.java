package be.kdg.processor.vehicle.web;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class VehicleDTO {
    private String plateId;
    private String nationalNumber;
    private int euroNumber;
}
