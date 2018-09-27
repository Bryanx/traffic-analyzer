package be.kdg.simulator.messaging.messengers;

import be.kdg.simulator.config.RecorderConfig;
import be.kdg.simulator.messaging.generators.MessageGenerator;
import lombok.AllArgsConstructor;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
@ConditionalOnProperty(name = "messenger.type", havingValue = "queue")
public class QueueMessenger implements Messenger {
    private final RabbitTemplate rabbitTemplate;
    private final RecorderConfig recorder;
    private final Queue queue;
    private final MessageGenerator messageGenerator;

    @Override
    public void sendMessage() {
        System.out.println("Sending message to queue");
        String msg = messageGenerator.generate().toString();
        rabbitTemplate.convertAndSend(queue.getName(), msg);
        recorder.record(String.format("Message was sent to queue: %s", msg));
    }
}
