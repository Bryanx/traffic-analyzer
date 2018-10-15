package be.kdg.processor.fine.evaluation;

import be.kdg.processor.camera.message.CameraMessage;
import be.kdg.processor.fine.Fine;
import be.kdg.processor.vehicle.Vehicle;

import java.util.List;

public interface FineEvaluationService {
    void checkForFine(CameraMessage cameraMessages);

    void createFine(Fine fine, Vehicle vehicle, List<CameraMessage> cameraMessages);

}
