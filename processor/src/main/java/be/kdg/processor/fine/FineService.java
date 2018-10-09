package be.kdg.processor.fine;

import be.kdg.processor.camera.message.CameraMessage;
import be.kdg.processor.vehicle.Vehicle;

import java.util.List;

public interface FineService {
    void checkForFine(List<CameraMessage> cameraMessages);

    void createFine(Fine fine, Vehicle vehicle, List<CameraMessage> cameraMessages);

}
