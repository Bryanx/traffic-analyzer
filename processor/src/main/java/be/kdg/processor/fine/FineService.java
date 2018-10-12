package be.kdg.processor.fine;

import be.kdg.processor.vehicle.Vehicle;

import java.time.LocalDateTime;
import java.util.List;

public interface FineService {
    Fine save(Fine fine);

    Fine findById(int id) throws FineException;

    List<Fine> findAll();

    List<Fine> findAllByVehicleIn(Vehicle vehicle);

    List<Fine> findAllByCreationDateBetween(LocalDateTime start, LocalDateTime end);

    void deleteById(int id);
}
