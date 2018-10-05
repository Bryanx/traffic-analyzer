package be.kdg.processor.vehicle;

import be.kdg.sa.services.LicensePlateNotFoundException;
import be.kdg.sa.services.LicensePlateServiceProxy;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;

@RequiredArgsConstructor
@Component
public class ProxyLicensePlateServiceImpl implements ProxyLicensePlateService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProxyLicensePlateServiceImpl.class);
    private final LicensePlateServiceProxy licensePlateServiceProxy;

    @Override
    public String get(String plate) {
        try {
            return licensePlateServiceProxy.get(plate);
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
        } catch (LicensePlateNotFoundException e) {
            LOGGER.warn(e.getMessage(), e);
        }
        return null;
    }
}
