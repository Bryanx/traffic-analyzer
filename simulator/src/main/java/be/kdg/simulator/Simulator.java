package be.kdg.simulator;

import be.kdg.simulator.messaging.generators.MessageGenerator;
import be.kdg.simulator.messaging.messengers.Messenger;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class Simulator {

    private final Messenger messenger;
    private final MessageGenerator generator;

    public void startSimulation() {
        messenger.sendMessage(generator.generate());
    }
}
