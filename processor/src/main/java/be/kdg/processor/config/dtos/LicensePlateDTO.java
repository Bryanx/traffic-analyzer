package be.kdg.processor.config.dtos;

import lombok.Data;

@Data
public class LicensePlateDTO {
    private String plateId;
    private String nationalNumber;
    private Integer euroNumber;
}
