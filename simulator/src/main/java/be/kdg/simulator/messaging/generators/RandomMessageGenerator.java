package be.kdg.simulator.messaging.generators;

import be.kdg.simulator.config.GeneratorConfig;
import be.kdg.simulator.model.CameraMessage;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Component
@ConditionalOnProperty(name = "generator.type", havingValue = "random")
public class RandomMessageGenerator implements MessageGenerator {
    private final GeneratorConfig generatorConfig;
    //Stored in a hashset because it is fast for adding and contains and it doesn't contain duplicates.
    private HashSet<Integer> allPossibleIds = new HashSet<>();
    private HashSet<String> allPossibleLicensePlates = new HashSet<>();
    private Random rnd = new Random();

    public RandomMessageGenerator(GeneratorConfig generatorConfig) {
        this.generatorConfig = generatorConfig;
    }

    @Override
    public CameraMessage generate() {
        return new CameraMessage(generateId(), generateLicensePlate(), LocalDateTime.now());
    }

    private int generateId() {
        while (allPossibleIds.size() < generatorConfig.getCount()) {
            int newId = rnd.nextInt(generatorConfig.getMaxid());
            if (allPossibleIds.add(newId)) {
                return newId;
            }
        }
        return 0;
    }

    private String generateLicensePlate() {
        while (allPossibleLicensePlates.size() < generatorConfig.getCount()) {
            String newPlate = String.format("%d-%c%c%c-%d%d%d",
                    rnd.nextInt(10),
                    getRandomChar(), getRandomChar(), getRandomChar(),
                    rnd.nextInt(10), rnd.nextInt(10), rnd.nextInt(10));
            if (allPossibleLicensePlates.add(newPlate)) {
                return newPlate;
            }
        }
        return "";
    }

    private char getRandomChar() {
        return (char) (rnd.nextInt(('Z' - 'A') + 1) + 'A');
    }
}
