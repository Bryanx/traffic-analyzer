package be.kdg.simulator.config;

import org.springframework.stereotype.Component;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class RecorderConfig {

    private static final String PATH = "simulator/log/recorder.log";

    public void record(String msg) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(PATH, true))) {
            writer.append(String.format("%s: %s%n",
                    LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-YYYY HH:mm:ss")),
                    msg));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
