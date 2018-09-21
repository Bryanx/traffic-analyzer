package be.kdg.simulator.messaging.messengers;

import be.kdg.simulator.messaging.generators.MessageGenerator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(name = "messenger.type", havingValue = "commandline")
public class CommandLineMessenger implements Messenger {

    private final MessageGenerator messageGenerator;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    public CommandLineMessenger(MessageGenerator messageGenerator) {
        this.messageGenerator = messageGenerator;
    }

    @Override
    public void sendMessage() {
        System.out.println(messageGenerator.generate() + " sent to console.");
    }
}
