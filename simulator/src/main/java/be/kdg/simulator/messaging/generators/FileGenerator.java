package be.kdg.simulator.messaging.generators;

import be.kdg.simulator.config.schedulers.SimulationScheduler;
import be.kdg.simulator.model.CameraMessage;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Stream;

@Component
@ConditionalOnProperty(name = "generator.type", havingValue = "file")
public class FileGenerator implements MessageGenerator {

    private static final String FILE_PATH = "simulator/src/main/resources/messages/cameramessages.csv";
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
                        messages.add(new CameraMessage(id, plate, LocalDateTime.now(), delay));
                    });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void scheduleNextMessage() {
        int delay = 2000;
        if (lineCounter == messages.size()) System.exit(0);
        if (lineCounter + 1 < messages.size()) delay = messages.get(lineCounter + 1).getDelay();
        simulationScheduler.stopSimulation();
        simulationScheduler.resetSimulationWithDelay(delay);
    }
}
