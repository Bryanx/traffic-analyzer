package be.kdg.simulator.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@ConfigurationProperties(prefix = "generator.message")
@Configuration
public class GeneratorConfig {
    private int maxid = 3;
    private long frequency = 1000;
    private String[] busyperiod;

    public void setFrequency(String frequency) {
        if (!frequency.equals("")) this.frequency = Long.parseLong(frequency);
    }

    public void setMaxid(String maxid) {
        if (!maxid.equals("")) this.maxid = Integer.parseInt(maxid);
    }
}
