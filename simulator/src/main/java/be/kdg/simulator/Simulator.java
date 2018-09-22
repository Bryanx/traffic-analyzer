package be.kdg.simulator;

import be.kdg.simulator.messaging.messengers.Messenger;
import org.springframework.stereotype.Component;

@Component
public class Simulator {

    private final Messenger messenger;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    public Simulator(Messenger messenger) {
        this.messenger = messenger;
    }

    public void startSimulation() {
        messenger.sendMessage();
    }
}
