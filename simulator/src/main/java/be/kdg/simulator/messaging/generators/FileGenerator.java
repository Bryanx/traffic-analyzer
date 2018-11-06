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
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@Component
@ConditionalOnProperty(name = "generator.type", havingValue = "file")
public class FileGenerator implements MessageGenerator {
    public static final Logger LOGGER = LoggerFactory.getLogger(FileGenerator.class);
    private static final String FOLDER_PATH = "simulator/files";
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
    public void readFolder() {
        try (Stream<Path> paths = Files.walk(Paths.get(FOLDER_PATH))) {
            paths.filter(Files::isRegularFile)
                    .forEach(path -> {
                        LOGGER.info("Started reading from file: " + path.toString());
                        readFile(path);
                    });
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    private void readFile(Path path) {
        try (Stream<String> lines = Files.lines(path)) {
            lines.map(line -> line.split(","))
                    .forEach(splittedLine -> {
                        int id = Integer.parseInt(splittedLine[0]);
                        String plate = splittedLine[1];
                        int delay = Integer.parseInt(splittedLine[2]);
                        messages.add(new CameraMessage(id, plate, LocalDateTime.now(), delay));
                    });
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    private void scheduleNextMessage() {
        if (lineCounter + 1 < messages.size()) {
            int delay = messages.get(lineCounter).getDelay();
            simulationScheduler.resetSimulationWithDelay(delay);
        }
    }
}
