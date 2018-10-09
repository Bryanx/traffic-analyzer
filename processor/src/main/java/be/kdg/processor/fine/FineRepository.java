package be.kdg.processor.fine;

import be.kdg.processor.vehicle.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FineRepository extends JpaRepository<Fine, Integer> {
    List<Fine> findAllByVehicleIn(Vehicle licenseplate);
}
