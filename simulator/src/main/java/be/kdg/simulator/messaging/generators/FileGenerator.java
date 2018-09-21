package be.kdg.simulator.messaging.generators;

import be.kdg.simulator.model.CameraMessage;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@ConditionalOnProperty(name = "generator.type", havingValue = "file")
public class FileGenerator implements MessageGenerator {
    @Override
    public CameraMessage generate() {
        return new CameraMessage(1, "1-FILE-123", LocalDateTime.now());
    }

    //TODO: Read file
}
