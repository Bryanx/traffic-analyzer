package be.kdg.processor.camera.proxy;

import be.kdg.processor.camera.Camera;
import be.kdg.processor.shared.converters.IoConverter;
import be.kdg.sa.services.CameraNotFoundException;
import be.kdg.sa.services.CameraServiceProxy;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;

@RequiredArgsConstructor
@Component
public class ProxyCameraServiceImpl implements ProxyCameraService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProxyCameraServiceImpl.class);
    private final CameraServiceProxy cameraServiceProxy;
    private final IoConverter ioConverter;

    public Camera get(int id) {
        try {
            String json = cameraServiceProxy.get(id);
            return ioConverter.readJson(json, Camera.class);
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
        } catch (CameraNotFoundException e) {
            LOGGER.warn(e.getMessage(), e);
        }
        return null;
    }
}
