package be.kdg.processor.services.api;

import be.kdg.processor.domain.Camera;
import be.kdg.processor.domain.CameraCouple;
import be.kdg.processor.domain.CameraMessage;

import java.util.List;

public interface CameraService {

    void receiveCameraMessage(String msg);
    CameraMessage createCameraMessage(CameraMessage cameraMessage);
    void deleteCameraMessage(CameraMessage cameraMessage);

    Camera createOrUpdateCamera(Camera camera);

    CameraCouple createOrUpdateCameraCouple(CameraCouple couple);
    List<CameraMessage> getCameraMessagesFromCouple(CameraCouple couple);
}