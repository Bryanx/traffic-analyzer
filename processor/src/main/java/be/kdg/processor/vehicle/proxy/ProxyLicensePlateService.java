package be.kdg.processor.vehicle.proxy;

import be.kdg.processor.vehicle.Vehicle;

import java.util.Optional;

public interface ProxyLicensePlateService {
    Optional<Vehicle> fetchVehicle(String plate);
}
