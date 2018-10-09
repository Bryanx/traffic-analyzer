package be.kdg.processor.camera.proxy;

import be.kdg.processor.camera.Camera;
import be.kdg.processor.camera.message.CameraMessage;

import java.util.Optional;

public interface ProxyCameraService {
    Optional<Camera> fetchCamera(CameraMessage message);
}
