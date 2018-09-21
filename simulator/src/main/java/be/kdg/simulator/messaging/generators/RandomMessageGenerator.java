package be.kdg.simulator.messaging.generators;

import be.kdg.simulator.model.CameraMessage;

import java.time.LocalDateTime;

public class RandomMessageGenerator implements MessageGenerator {

    @Override
    public CameraMessage generate() {
        return new CameraMessage(1, "1-RND-123", LocalDateTime.now());
    }
}
