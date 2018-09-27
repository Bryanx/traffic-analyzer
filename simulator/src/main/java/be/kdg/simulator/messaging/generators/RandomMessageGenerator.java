package be.kdg.simulator.messaging.generators;

import be.kdg.simulator.config.GeneratorConfig;
import be.kdg.simulator.config.schedulers.SimulationScheduler;
import be.kdg.simulator.model.CameraMessage;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.*;

@AllArgsConstructor
@Component
@ConditionalOnProperty(name = "generator.type", havingValue = "random")
public class RandomMessageGenerator implements MessageGenerator {
    private static final Logger LOGGER = LoggerFactory.getLogger(RandomMessageGenerator.class);
    private static final String ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final Random rnd = new Random();
    private final GeneratorConfig generatorConfig;

    @Override
    public CameraMessage generate() {
        int cameraId = rnd.nextInt(generatorConfig.getMaxid()) + 1;
        CameraMessage message = new CameraMessage(cameraId, generateLicensePlate(), LocalDateTime.now());
        LOGGER.info("Generated new CameraMessage {}", message);
        return message;
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
