package be.kdg.simulator.config;

import be.kdg.simulator.messaging.generators.FileGenerator;
import be.kdg.simulator.messaging.generators.MessageGenerator;
import be.kdg.simulator.messaging.generators.RandomMessageGenerator;
import be.kdg.simulator.messaging.messengers.CommandLineMessenger;
import be.kdg.simulator.messaging.messengers.QueueMessenger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@Configuration
public class GeneratorConfig {

    @Bean
    @ConditionalOnProperty(name = "generator.type", havingValue = "file")
    public MessageGenerator fileGenerator() {
        return new FileGenerator();
    }

    @Bean
    @ConditionalOnProperty(name = "generator.type", havingValue = "random")
    public MessageGenerator randomMessageGenerator() {
        return new RandomMessageGenerator();
    }
}
