package be.kdg.simulator.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.amqp.core.Queue;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@ConfigurationProperties(prefix = "queue")
@Configuration
public class QueueConfig {
    private String name = "camera-message-queue";

    @Bean
    Queue queue() {
        return new Queue(name, false);
    }

    public void setName(String name) {
        if (!name.equals("")) this.name = name;
    }
}
