package be.kdg.simulator;

import be.kdg.simulator.config.GeneratorConfig;
import be.kdg.simulator.messaging.messengers.Messenger;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class Simulator {

    private final Messenger messenger;
    private final GeneratorConfig generatorConfig;

    private int currentCount = 0;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    public Simulator(Messenger messenger, GeneratorConfig generatorConfig) {
        this.messenger = messenger;
        this.generatorConfig = generatorConfig;
    }

    @Scheduled(fixedDelay = 1000L)
    public void startSimulation() {
        if (currentCount < generatorConfig.getCount()) {
            messenger.sendMessage();
            currentCount++;
        } else {
            System.exit(0);
        }
    }
}
