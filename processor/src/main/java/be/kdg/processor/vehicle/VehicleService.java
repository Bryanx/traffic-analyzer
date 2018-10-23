package be.kdg.processor.vehicle;

import be.kdg.processor.shared.exception.ProcessorException;

public interface VehicleService {
    Vehicle createVehicle(Vehicle vehicle);
    Vehicle findByLicensePlate(String licenseplate) throws ProcessorException;
    Vehicle getVehicleByProxyOrDb(String licensePlate) throws ProcessorException;
}
