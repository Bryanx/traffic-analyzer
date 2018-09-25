package be.kdg.simulator.messaging.messengers;

import be.kdg.simulator.messaging.generators.MessageGenerator;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(name = "messenger.type", havingValue = "queue")
public class QueueMessenger implements Messenger {
    private final RabbitTemplate rabbitTemplate;
    private Queue queue;
    private final MessageGenerator messageGenerator;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    public QueueMessenger(MessageGenerator messageGenerator, RabbitTemplate rabbitTemplate, Queue queue) {
        this.messageGenerator = messageGenerator;
        this.rabbitTemplate = rabbitTemplate;
        this.queue = queue;
    }

    @Override
    public void sendMessage() {
        System.out.println("Sending message to queue");
        rabbitTemplate.convertAndSend(queue.getName(),messageGenerator.generate().toString());
    }

}
