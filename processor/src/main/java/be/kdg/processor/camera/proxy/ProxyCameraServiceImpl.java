package be.kdg.processor.camera.proxy;

import be.kdg.processor.camera.Camera;
import be.kdg.processor.camera.message.CameraMessage;
import be.kdg.processor.shared.converters.IoConverter;
import be.kdg.sa.services.CameraNotFoundException;
import be.kdg.sa.services.CameraServiceProxy;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Optional;

@RequiredArgsConstructor
@Component
public class ProxyCameraServiceImpl implements ProxyCameraService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProxyCameraServiceImpl.class);
    private final CameraServiceProxy cameraServiceProxy;
    private final IoConverter ioConverter;
    private final RetryTemplate retryTemplate;

    @Override
    public Optional<Camera> fetchCamera(CameraMessage message) {
        try {
            String json = retryTemplate.execute(ctx -> {
                if (ctx.getRetryCount() > 1)
                    LOGGER.debug("Retrying camera, count: " + ctx.getRetryCount() + ". For message {}", message);
                return fetchCameraFromProxy(message.getCameraId());
            });
            Optional<Camera> optionalCamera = ioConverter.readJson(json, Camera.class);
            optionalCamera.ifPresent(camera -> camera.addCameraMessage(message));
            return optionalCamera;
        } catch (IOException | CameraNotFoundException e) {
            LOGGER.warn(e.getMessage());
        }
        return Optional.empty();
    }

    @Cacheable("cameras")
    public String fetchCameraFromProxy(int id) throws IOException {
        return cameraServiceProxy.get(id);
    }

}
