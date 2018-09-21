package be.kdg.simulator.messengers;

import be.kdg.simulator.generators.MessageGenerator;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
//Probleem met DI: Welke bean moet worden geinject?
//Kan je oplossen met @Qualifier
//Je kan ook boven de bean een extra annotatie toevoegen ConditionalOnProperty
//Hierdoor kan je in de properties file specifieren welke bean je wilt gebruiken.
public class CommandLineMessenger implements Messenger {

    private MessageGenerator messageGenerator;

    public CommandLineMessenger(MessageGenerator messageGenerator) {
        this.messageGenerator = messageGenerator;
    }

    @Override
    public void sendMessage() {
        System.out.println(messageGenerator.generate());
    }
}
