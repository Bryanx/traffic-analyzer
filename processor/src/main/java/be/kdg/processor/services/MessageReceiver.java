package be.kdg.processor.services;

import be.kdg.processor.config.converters.XmlConverter;
import be.kdg.processor.domain.CameraMessage;
import be.kdg.processor.persistence.api.ICameraMessageRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
@RabbitListener(queues = "camera-message-queue")
public class MessageReceiver {

    private final XmlConverter xmlConverter;
    private final ICameraMessageRepo cameraMessageRepo;

    @RabbitHandler
    public void receive(String msg) {
        CameraMessage message = xmlConverter.xmlToCameraMessage(msg);
        cameraMessageRepo.save(message);
        System.out.println("Received: " + message);
    }
}
