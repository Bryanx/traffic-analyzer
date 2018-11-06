package be.kdg.simulator;

import be.kdg.simulator.messaging.generators.MessageGenerator;
import be.kdg.simulator.messaging.messengers.Messenger;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * Starts the simulation in either file/random mode and posts them to the commandline/queue.
 */
@AllArgsConstructor
@Component
public class Simulator {

    private final Messenger messenger;
    private final MessageGenerator generator;

    public void startSimulation() {
        messenger.sendMessage(generator.generate());
    }
}
