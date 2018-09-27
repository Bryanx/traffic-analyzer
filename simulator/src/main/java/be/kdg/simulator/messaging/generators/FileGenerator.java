package be.kdg.simulator.messaging.generators;

import be.kdg.simulator.config.schedulers.SimulationScheduler;
import be.kdg.simulator.model.CameraMessage;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Lazy;
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
        if (lineCounter >= messages.size()) System.exit(0);
        CameraMessage message = messages.get(lineCounter++);
        try {
            //TODO: Changing the scheduler delay from here doesn't seem to be working, this is a temp fix.
            if (lineCounter == 0) simulationScheduler.resetSimulation(0);
            Thread.sleep(message.getDelay());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        message.setTimestamp(LocalDateTime.now());
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
}
