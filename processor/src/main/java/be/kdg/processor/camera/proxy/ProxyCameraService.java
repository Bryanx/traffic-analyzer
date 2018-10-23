package be.kdg.processor.camera.proxy;

import be.kdg.processor.camera.Camera;
import be.kdg.processor.camera.message.CameraMessage;
import be.kdg.processor.shared.exception.ProcessorException;

public interface ProxyCameraService {
    Camera fetchCamera(CameraMessage message) throws ProcessorException;
}
