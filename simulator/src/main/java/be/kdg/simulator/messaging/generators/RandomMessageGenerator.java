package be.kdg.simulator.messaging.generators;

import be.kdg.simulator.config.GeneratorConfig;
import be.kdg.simulator.model.CameraMessage;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@ConditionalOnProperty(name = "generator.type", havingValue = "random")
public class RandomMessageGenerator implements MessageGenerator {

    @Override
    public CameraMessage generate() {
        return new CameraMessage(1, "1-RND-123", LocalDateTime.now());
    }
}
