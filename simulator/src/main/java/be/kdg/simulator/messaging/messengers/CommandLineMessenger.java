package be.kdg.simulator.messaging.messengers;

import be.kdg.simulator.messaging.generators.MessageGenerator;
import lombok.AllArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
@ConditionalOnProperty(name = "messenger.type", havingValue = "commandline")
public class CommandLineMessenger implements Messenger {

    private final MessageGenerator messageGenerator;

    @Override
    public void sendMessage() {
        System.out.println(messageGenerator.generate() + " sent to console.");
    }
}
