package be.kdg.processor.services.api;

import be.kdg.processor.domain.CameraCouple;
import be.kdg.processor.domain.CameraMessage;

import java.util.List;

public interface FineService {
    void checkForFine(CameraCouple couple, List<CameraMessage> msgs);
}
