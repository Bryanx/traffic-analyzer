package be.kdg.processor.vehicle;

import java.util.Optional;

public interface ProxyLicensePlateService {
    Optional<Vehicle> get(String plate);
}
