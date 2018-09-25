package be.kdg.simulator.messaging.generators;

import be.kdg.simulator.model.CameraMessage;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.stream.Stream;

@Component
@ConditionalOnProperty(name = "generator.type", havingValue = "file")
public class FileGenerator implements MessageGenerator {

    @Override
    public CameraMessage generate() {
        return new CameraMessage(1, "1-FILE-123", LocalDateTime.now());
    }

    //TODO: Read file
    public void readFile() throws IOException, URISyntaxException {
        Path path = Paths.get(getClass().getClassLoader().getResource("fileTest.txt").toURI());

        StringBuilder data = new StringBuilder();
        Stream<String> lines = Files.lines(path);
        lines.forEach(line -> data.append(line).append("\n"));
        lines.close();
    }
}
