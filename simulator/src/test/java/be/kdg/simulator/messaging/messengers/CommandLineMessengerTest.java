package be.kdg.simulator.messaging.messengers;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class) //hierdoor wordt er automatisch een spring container gemaakt, daardoor is DI mogelijk
@SpringBootTest
public class CommandLineMessengerTest {

    @Autowired
    private Messenger messenger;

    @Test
    public void sendMessage() {
        messenger.sendMessage();
    }
}