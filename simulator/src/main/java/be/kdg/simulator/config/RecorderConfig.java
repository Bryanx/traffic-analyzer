package be.kdg.simulator.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Configuration
public class RecorderConfig {

    private String path = "simulator/log/recorder.log";
    private int tryCount = 0;
    private int maxTries = 3;

    public void record(String msg) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(path, true))) {
            writer.append(String.format("%s: %s%n",
                    LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-YYYY HH:mm:ss")),
                    msg));
        } catch (IOException e) {
            //try with a different path
            path = "log/recorder.log";
            if (++tryCount == maxTries) e.printStackTrace();
        }
    }
}
