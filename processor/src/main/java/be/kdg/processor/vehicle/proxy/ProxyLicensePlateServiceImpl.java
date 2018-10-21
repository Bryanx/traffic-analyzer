package be.kdg.processor.vehicle.proxy;

import be.kdg.processor.shared.converters.IoConverter;
import be.kdg.processor.vehicle.Vehicle;
import be.kdg.sa.services.LicensePlateNotFoundException;
import be.kdg.sa.services.LicensePlateServiceProxy;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Optional;

@RequiredArgsConstructor
@Component
public class ProxyLicensePlateServiceImpl implements ProxyLicensePlateService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProxyLicensePlateServiceImpl.class);
    private final LicensePlateServiceProxy licensePlateServiceProxy;
    private final IoConverter ioConverter;
    private final RetryTemplate retryTemplate;

    //    @Cacheable("vehicles")
    @Override
    public Optional<Vehicle> fetchVehicle(String plate) {
        try {
            String json = retryTemplate.execute(ctx -> {
                if (ctx.getRetryCount() > 1) LOGGER.debug("Retrying vehicle {}, retry count: " + ctx.getRetryCount(), plate);
                return licensePlateServiceProxy.get(plate);
            });
            return ioConverter.readJson(json, Vehicle.class);
        } catch (IOException | LicensePlateNotFoundException e) {
            LOGGER.warn(e.getMessage());
        }
        return Optional.empty();
    }
}
