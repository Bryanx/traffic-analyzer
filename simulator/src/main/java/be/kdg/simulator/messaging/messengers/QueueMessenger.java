package be.kdg.simulator.messaging.messengers;

import be.kdg.simulator.config.RecorderConfig;
import be.kdg.simulator.messaging.generators.FileGenerator;
import be.kdg.simulator.messaging.generators.MessageGenerator;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
@ConditionalOnProperty(name = "messenger.type", havingValue = "queue")
public class QueueMessenger implements Messenger {
    public static final Logger LOGGER = LoggerFactory.getLogger(QueueMessenger.class);
    private final RabbitTemplate rabbitTemplate;
    private final RecorderConfig recorder;
    private final Queue queue;
    private final MessageGenerator messageGenerator;

    @Override
    public void sendMessage() {
        String msg = messageGenerator.generate().toString();
        LOGGER.info("Sending message to queue: ", msg);
        recorder.record(String.format("Sending message to queue: %s", msg));
        rabbitTemplate.convertAndSend(queue.getName(), msg);
    }
}
