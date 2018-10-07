package be.kdg.processor.camera;

import be.kdg.processor.camera.message.CameraMessage;

public interface CameraService {

    void receiveCameraMessage(CameraMessage msg);
    CameraMessage createCameraMessage(CameraMessage cameraMessage);
    void deleteCameraMessage(CameraMessage cameraMessage);

    Camera createOrUpdateCamera(Camera camera);

    void emptyBuffer();
}