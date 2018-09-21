package be.kdg.simulator;

import be.kdg.simulator.config.MessengerConfig;
import be.kdg.simulator.messaging.messengers.Messenger;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class Simulator {

    private final Messenger messenger;
    private final MessengerConfig messengerConfig;

    private int currentCount = 0;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    public Simulator(Messenger messenger, MessengerConfig messengerConfig) {
        this.messenger = messenger;
        this.messengerConfig = messengerConfig;
    }

    @Scheduled(fixedDelay = 1000L)
    public void startSimulation() {
        if (currentCount < messengerConfig.getRandomMessageCount()) {
            messenger.sendMessage();
        } else {
            System.exit(0);
        }
        currentCount++;
    }
}
