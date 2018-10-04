package be.kdg.processor.services.ThirdParty;

import be.kdg.sa.services.CameraNotFoundException;
import be.kdg.sa.services.CameraServiceProxy;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@AllArgsConstructor
public class ProxyCameraServiceImpl implements ProxyCameraService {

    private CameraServiceProxy cameraServiceProxy;
    public static final Logger LOGGER = LoggerFactory.getLogger(ProxyCameraServiceImpl.class);

    public String get(int id) {
        try {
            return cameraServiceProxy.get(id);
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
        } catch (CameraNotFoundException e) {
            LOGGER.warn(e.getMessage(), e);
        }
        return null;
    }
}
