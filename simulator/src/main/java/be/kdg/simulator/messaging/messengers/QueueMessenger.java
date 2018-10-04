package be.kdg.simulator.messaging.messengers;

import be.kdg.simulator.config.RecorderConfig;
import be.kdg.simulator.config.converters.XmlConverter;
import be.kdg.simulator.messaging.generators.MessageGenerator;
import be.kdg.simulator.model.CameraMessage;
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
    private final XmlConverter xmlConverter;

    @Override
    public void sendMessage() {
        CameraMessage msg = messageGenerator.generate();
        LOGGER.info("Sending message to queue: {}", msg);
        recorder.record(String.format("Sending message to queue: %s", msg));
        String xmlConverted = xmlConverter.objectToXML(msg);
        rabbitTemplate.convertAndSend(queue.getName(), xmlConverted);
    }
}
