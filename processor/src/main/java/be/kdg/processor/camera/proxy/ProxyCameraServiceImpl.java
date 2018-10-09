package be.kdg.processor.camera.proxy;

import be.kdg.processor.camera.Camera;
import be.kdg.processor.camera.message.CameraMessage;
import be.kdg.processor.shared.converters.IoConverter;
import be.kdg.sa.services.CameraNotFoundException;
import be.kdg.sa.services.CameraServiceProxy;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Optional;

@RequiredArgsConstructor
@Component
public class ProxyCameraServiceImpl implements ProxyCameraService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProxyCameraServiceImpl.class);
    private final CameraServiceProxy cameraServiceProxy;
    private final IoConverter ioConverter;

    //TODO: Add cache
    @Override
    public Optional<Camera> fetchCamera(CameraMessage message) {
        try {
            String json = cameraServiceProxy.get(message.getCameraId());
            Camera camera = ioConverter.readJson(json, Camera.class);
            camera.addCameraMessage(message);
            return Optional.of(camera);
        } catch (IOException e) {
            LOGGER.warn("Camera with id {} forced a communication error.", message.getCameraId());
        } catch (CameraNotFoundException e) {
            LOGGER.warn("Camera with id {} not found.", message.getCameraId());
        }
        return Optional.empty();
    }

}
