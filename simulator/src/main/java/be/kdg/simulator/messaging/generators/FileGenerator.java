package be.kdg.simulator.messaging.generators;

import be.kdg.simulator.config.schedulers.SimulationScheduler;
import be.kdg.simulator.model.CameraMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@Component
@ConditionalOnProperty(name = "generator.type", havingValue = "file")
public class FileGenerator implements MessageGenerator {
    public static final Logger LOGGER = LoggerFactory.getLogger(FileGenerator.class);
    private static final String FILE_PATH = "simulator/files/cameramessages.csv";
    private final SimulationScheduler simulationScheduler;
    private List<CameraMessage> messages = new ArrayList<>();
    public static int lineCounter = 0;

    public FileGenerator(@Lazy SimulationScheduler simulationScheduler) {
        this.simulationScheduler = simulationScheduler;
    }

    @Override
    public CameraMessage generate() {
        scheduleNextMessage();
        CameraMessage message = messages.get(lineCounter);
        message.setTimestamp(LocalDateTime.now());
        lineCounter++;
        return message;
    }

    @PostConstruct
    public void readFile() {
        try (Stream<String> lines = Files.lines(Paths.get(FILE_PATH))) {
            lines.map(line -> line.split(","))
                    .forEach(splittedLine -> {
                        int id = Integer.parseInt(splittedLine[0]);
                        String plate = splittedLine[1];
                        int delay = Integer.parseInt(splittedLine[2]);
                        CameraMessage newMsg = new CameraMessage(id, plate, LocalDateTime.now(), delay);
                        if (messages.add(newMsg)) {
                            LOGGER.info("Message succesfully loaded: {}", newMsg);
                        }
                    });
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    private void scheduleNextMessage() {
        int delay = 2000;
        if (lineCounter != messages.size()) return;
        if (lineCounter + 1 < messages.size()) {
            delay = messages.get(lineCounter + 1).getDelay();
        }
        simulationScheduler.resetSimulationWithDelay(delay);
    }
}
