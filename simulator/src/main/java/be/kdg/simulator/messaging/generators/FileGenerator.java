package be.kdg.simulator.messaging.generators;

import be.kdg.simulator.model.CameraMessage;
import java.time.LocalDateTime;

public class FileGenerator implements MessageGenerator {
    @Override
    public CameraMessage generate() {
        return new CameraMessage(1, "1-FILE-123", LocalDateTime.now());
    }

    //TODO: Read file
}
