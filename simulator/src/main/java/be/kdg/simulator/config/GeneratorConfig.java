package be.kdg.simulator.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

@Getter
@Setter
@EnableScheduling
@ConfigurationProperties(prefix = "generator.message")
@Configuration
public class GeneratorConfig {
    private int maxid = 10;
    private long frequency = 1000;
    private String[] busyperiod;

    @Bean
    public TaskScheduler configureTasks() {
        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
        scheduler.setPoolSize(100);
        scheduler.initialize();
        return scheduler;
    }

    public void setFrequency(String frequency) {
        if (!frequency.equals("")) this.frequency = Long.parseLong(frequency);
    }

    public void setMaxid(String maxid) {
        if (!maxid.equals("")) this.maxid = Integer.parseInt(maxid);
    }
}
