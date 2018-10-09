package be.kdg.processor.vehicle;

import java.util.Optional;

public interface VehicleService {
    Vehicle createVehicle(Vehicle vehicle);
    Optional<Vehicle> findByLicensePlate(String licenseplate);
    Optional<Vehicle> getVehicleByProxyOrDb(String licensePlate);
}
