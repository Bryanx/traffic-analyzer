package be.kdg.simulator.messaging.messengers;

import be.kdg.simulator.model.CameraMessage;
import lombok.AllArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
@ConditionalOnProperty(name = "messenger.type", havingValue = "commandline")
public class CommandLineMessenger implements Messenger {

    @Override
    public void sendMessage(CameraMessage msg) {
        System.out.println(msg + " sent to console.");
    }
}
