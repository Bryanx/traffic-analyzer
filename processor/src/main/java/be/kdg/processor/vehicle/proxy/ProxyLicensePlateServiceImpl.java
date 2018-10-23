package be.kdg.processor.vehicle.proxy;

import be.kdg.processor.shared.converters.IoConverter;
import be.kdg.processor.shared.exception.ProcessorException;
import be.kdg.processor.vehicle.Vehicle;
import be.kdg.sa.services.LicensePlateNotFoundException;
import be.kdg.sa.services.LicensePlateServiceProxy;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Component;

import java.io.IOException;

@RequiredArgsConstructor
@Component
public class ProxyLicensePlateServiceImpl implements ProxyLicensePlateService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProxyLicensePlateServiceImpl.class);
    private final LicensePlateServiceProxy licensePlateServiceProxy;
    private final IoConverter ioConverter;
    private final RetryTemplate retryTemplate;

    @Override
    public Vehicle fetchVehicle(String plate) throws ProcessorException {
        try {
            String json = retryTemplate.execute(ctx -> {
                if (ctx.getRetryCount() > 1) LOGGER.debug("Retrying vehicle {}, retry count: " + ctx.getRetryCount(), plate);
                return fetchVehicleFromProxy(plate);
            });
            return ioConverter.readJson(json, Vehicle.class);
        } catch (IOException | LicensePlateNotFoundException e) {
            throw new ProcessorException(e.getMessage());
        }
    }

    @Cacheable("vehicles")
    public String fetchVehicleFromProxy(String plate) throws IOException {
        return licensePlateServiceProxy.get(plate);
    }
}
