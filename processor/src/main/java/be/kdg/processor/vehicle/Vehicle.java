package be.kdg.processor.vehicle;

import be.kdg.processor.fine.Fine;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class Vehicle {
    @Id
    private String plateId;
    private String nationalNumber;
    private int euroNumber;

    @OneToMany(targetEntity = Fine.class)
    private List<Fine> fines = new ArrayList<>();

    public void addFine(Fine fine) {
        fines.add(fine);
        fine.setVehicle(this);
    }
}
