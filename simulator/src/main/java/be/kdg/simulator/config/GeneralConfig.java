package be.kdg.simulator.config;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

@RequiredArgsConstructor
@EnableScheduling
@Configuration
public class GeneralConfig {

    private static final int POOL_SIZE = 100;

    @Bean
    public TaskScheduler configureTasks() {
        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
        scheduler.setPoolSize(POOL_SIZE);
        scheduler.initialize();
        return scheduler;
    }

    @Bean
    public XmlMapper xmlMapper() {
        return new XmlMapper();
    }

}
