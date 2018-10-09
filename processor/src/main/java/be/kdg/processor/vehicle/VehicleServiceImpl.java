package be.kdg.processor.vehicle;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class VehicleServiceImpl implements VehicleService {
    private static final Logger LOGGER = LoggerFactory.getLogger(VehicleServiceImpl.class);
    private final VehicleRepository vehicleRepository;
    private final ProxyLicensePlateService proxyLicensePlateService;

    @Override
    public Vehicle createVehicle(Vehicle vehicle) {
        LOGGER.debug("Added vehicle to db with licenseplate: {}", vehicle.getPlateId());
        return vehicleRepository.saveAndFlush(vehicle);
    }

    @Override
    public Optional<Vehicle> findByLicensePlate(String licenseplate) {
        return vehicleRepository.findById(licenseplate);
    }

    @Override
    public Vehicle getVehicleByProxyOrDb(String licensePlate) {
        Optional<Vehicle> byLicensePlate = findByLicensePlate(licensePlate);
        if (byLicensePlate.isPresent()) {
            LOGGER.debug("Got vehicle from db: {}", byLicensePlate.get());
            return byLicensePlate.get();
        }
        LOGGER.debug("Got vehicle from proxy");
        return proxyLicensePlateService.get(licensePlate);
    }

}
