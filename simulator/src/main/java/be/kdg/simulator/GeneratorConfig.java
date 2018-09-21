package be.kdg.simulator;

import be.kdg.simulator.generators.FileGenerator;
import be.kdg.simulator.generators.MessageGenerator;
import be.kdg.simulator.generators.RandomMessageGenerator;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GeneratorConfig {

    @Bean
    @ConditionalOnProperty(name = "load", havingValue = "file")
    public MessageGenerator fileGenerator() {
        return new FileGenerator();
    }

    @Bean
    @ConditionalOnProperty(name = "load", havingValue = "random")
    public MessageGenerator randomMessageGenerator() {
        return new RandomMessageGenerator();
    }


}
