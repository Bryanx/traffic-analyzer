package be.kdg.simulator.config;

import be.kdg.simulator.config.schedulers.SimulationScheduler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Class for storing messages that were sent to the queue in a seperate file.
 */
@Configuration
public class RecorderConfig {
    private static final Logger LOGGER = LoggerFactory.getLogger(RecorderConfig.class);
    private String path = "simulator/log/recorder.log";
    private int tryCount = 1;
    private int maxTries = 3;

    public void record(String msg) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(path, true))) {
            writer.append(String.format("%s: %s%n",
                    LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-YYYY HH:mm:ss")),
                    msg));
        } catch (IOException e) {
            handleIOException(e);
        }
    }

    private void handleIOException(IOException e) {
        LOGGER.warn("Failed create/update recorder log file in path: '" + path);
        if (tryCount == 1) {
            path = "log/recorder.log";
            LOGGER.warn("Retrying with a different path: " + path);
        }
        if (++tryCount == maxTries) LOGGER.error(e.getMessage(),e);
    }
}
