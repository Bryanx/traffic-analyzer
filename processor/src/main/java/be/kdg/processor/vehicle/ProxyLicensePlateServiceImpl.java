package be.kdg.processor.vehicle;

import be.kdg.processor.shared.converters.IoConverter;
import be.kdg.sa.services.LicensePlateNotFoundException;
import be.kdg.sa.services.LicensePlateServiceProxy;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Optional;

@RequiredArgsConstructor
@Component
public class ProxyLicensePlateServiceImpl implements ProxyLicensePlateService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProxyLicensePlateServiceImpl.class);
    private final LicensePlateServiceProxy licensePlateServiceProxy;
    private final IoConverter ioConverter;

    @Override
    public Optional<Vehicle> get(String plate) {
        try {
            String json = licensePlateServiceProxy.get(plate);
            Vehicle vehicle = ioConverter.readJson(json, Vehicle.class);
            return Optional.of(vehicle);
        } catch (IOException e) {
            LOGGER.warn("Licenseplate proxy with id {} forced a communication error.", plate);
        } catch (LicensePlateNotFoundException e) {
            LOGGER.warn("Licenseplate with id {} not found.", plate);
        }
        return Optional.empty();
    }
}
