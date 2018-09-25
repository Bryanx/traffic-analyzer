package be.kdg.simulator.messaging.generators;

import be.kdg.simulator.config.GeneratorConfig;
import be.kdg.simulator.model.CameraMessage;
import lombok.AllArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.*;

@AllArgsConstructor
@Component
@ConditionalOnProperty(name = "generator.type", havingValue = "random")
public class RandomMessageGenerator implements MessageGenerator {
    private static final String ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final Random rnd = new Random();
    private final GeneratorConfig generatorConfig;

    @Override
    public CameraMessage generate() {
        int cameraId = rnd.nextInt(generatorConfig.getMaxid()) + 1;
        return new CameraMessage(cameraId, generateLicensePlate(), LocalDateTime.now());
    }

    /**
     * Generates a random license plate: [1-8] - [A-Z]3x - [0-9][0-9][1-9]
     */
    private String generateLicensePlate() {
        return String.format("%d-%c%c%c-%d%d%d",
                rnd.nextInt(8) + 1,
                getRandomChar(), getRandomChar(), getRandomChar(),
                rnd.nextInt(10), rnd.nextInt(10), rnd.nextInt(10));
    }

    private char getRandomChar() {
        return ALPHABET.charAt(rnd.nextInt(ALPHABET.length()));
    }
}
