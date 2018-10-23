package be.kdg.processor.vehicle;

import be.kdg.processor.shared.exception.ProcessorException;
import be.kdg.processor.vehicle.proxy.ProxyLicensePlateService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

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
    public Vehicle findByLicensePlate(String licenseplate) throws ProcessorException {
        return vehicleRepository.findById(licenseplate)
                .orElseThrow(() -> new ProcessorException("Licenseplate not found in db: " + licenseplate));
    }

    @Override
    public Vehicle getVehicleByProxyOrDb(String licensePlate) throws ProcessorException {
        try {
            Vehicle vehicle = findByLicensePlate(licensePlate);
            LOGGER.debug("Got vehicle from db: {}", vehicle);
            return vehicle;
        } catch (ProcessorException e) {
            LOGGER.debug("Got vehicle from proxy");
            return proxyLicensePlateService.fetchVehicle(licensePlate);
        }
    }

}
