package be.kdg.simulator.config;

import be.kdg.simulator.config.schedulers.SimulationScheduler;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Queue;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@ConfigurationProperties(prefix = "queue")
@Configuration
public class QueueConfig {
    private static final Logger LOGGER = LoggerFactory.getLogger(QueueConfig.class);
    private String name = "camera-message-queue";

    @Bean
    Queue queue() {
        LOGGER.info("Creating new queue with name: " + name);
        return new Queue(name, false);
    }

    public void setName(String name) {
        if (!name.equals("")) this.name = name;
    }
}
