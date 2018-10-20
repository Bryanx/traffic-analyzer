package be.kdg.processor.vehicle;

import be.kdg.processor.fine.Fine;
import lombok.Data;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.CascadeType;
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

    @LazyCollection(LazyCollectionOption.FALSE)
    @OneToMany(targetEntity = Fine.class, cascade = CascadeType.ALL, mappedBy = "vehicle")
    private List<Fine> fines = new ArrayList<>();

    public void addFine(Fine fine) {
        fines.add(fine);
        fine.setVehicle(this);
    }
}
