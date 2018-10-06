package be.kdg.processor.camera;

import be.kdg.processor.camera.couple.CameraCouple;
import be.kdg.processor.camera.message.CameraMessage;

import java.util.List;

public interface CameraService {

    void receiveCameraMessage(CameraMessage msg);
    CameraMessage createCameraMessage(CameraMessage cameraMessage);
    void deleteCameraMessage(CameraMessage cameraMessage);

    Camera createOrUpdateCamera(Camera camera);

    CameraCouple createOrUpdateCameraCouple(CameraCouple couple);
    List<CameraMessage> getCameraMessagesFromCouple(CameraCouple couple);

    void emptyBuffer();
}