package be.kdg.processor.vehicle;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Data
public class Vehicle {
    @Id
    private String plateId;
    private String nationalNumber;
    private int euroNumber;
}
