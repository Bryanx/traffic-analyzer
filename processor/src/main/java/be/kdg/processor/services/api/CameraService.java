package be.kdg.processor.services.api;

import be.kdg.processor.domain.CameraMessage;

public interface CameraService {
    void receiveCameraMessage(String msg);
    CameraMessage createCameraMessage(CameraMessage cameraMessage);
    void deleteCameraMessage(CameraMessage cameraMessage);

}