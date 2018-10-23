package be.kdg.processor.vehicle.proxy;

import be.kdg.processor.shared.exception.ProcessorException;
import be.kdg.processor.vehicle.Vehicle;

public interface ProxyLicensePlateService {
    Vehicle fetchVehicle(String plate) throws ProcessorException;
}
