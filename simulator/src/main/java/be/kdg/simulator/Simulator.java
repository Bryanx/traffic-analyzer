package be.kdg.simulator;

import be.kdg.simulator.messaging.messengers.Messenger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class Simulator {

    private final Messenger messenger;

    @Value("${generator.random.messagecount}")
    private int randomMessageCount;
    private int currentCount = 0;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    public Simulator(Messenger messenger) {
        this.messenger = messenger;
    }

    @Scheduled(fixedDelay = 1000L)
    public void startSimulation() {
        if (currentCount < randomMessageCount) {
            messenger.sendMessage();
        } else {
            System.exit(0);
        }
        currentCount++;
    }
}
