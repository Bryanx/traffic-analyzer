package be.kdg.processor.camera.message.receiver;

import be.kdg.processor.camera.CameraService;
import be.kdg.processor.camera.message.CameraMessage;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class CameraMessageReceiver implements Receiver<CameraMessage> {
    private static final Logger LOGGER = LoggerFactory.getLogger(CameraMessageReceiver.class);
    private List<CameraMessage> cameraMessages = new ArrayList<>();
    private final CameraService cameraService;

    @Override
    @RabbitListener(queues = "camera-message-queue")
    public void receive(@Payload CameraMessage message) {
        LOGGER.info("Received message: {}", message);
//        cameraMessages.add(message);
        persist(message);
    }

    @Override
    public List<CameraMessage> empty() {
        final List<CameraMessage> temp = cameraMessages;
        cameraMessages = new ArrayList<>();
        return temp;
    }

    @Override
    @Transactional
    public void persist(CameraMessage message) {
        cameraService.saveCameraWithSegment(message);
    }
}
