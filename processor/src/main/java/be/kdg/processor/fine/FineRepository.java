package be.kdg.processor.fine;

import be.kdg.processor.vehicle.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface FineRepository extends JpaRepository<Fine, Integer> {
    List<Fine> findAllByTypeEqualsAndVehicleIn(FineType fineType, Vehicle licenseplate);

    List<Fine> findAllByCreationDateBetween(LocalDateTime start, LocalDateTime end);
}
