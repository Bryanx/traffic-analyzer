package be.kdg.simulator.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@Getter
@Setter
@EnableScheduling
@ConfigurationProperties(prefix = "generator.message")
@Configuration
public class GeneratorConfig {
    private int count;
    private int maxid;
}
