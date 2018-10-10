package be.kdg.processor.fine;

import be.kdg.processor.vehicle.Vehicle;

import java.util.List;

public interface FineService {
    Fine saveAndFlush(Fine fine);

    Fine findById(int id) throws FineException;

    List<Fine> findAllByVehicleIn(Vehicle vehicle);
}
