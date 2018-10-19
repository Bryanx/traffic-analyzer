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

/**
 * Receives CamerMessages from the queue.
 * The CameraMessages can be buffered in the database or in memory.
 */
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
        bufferInDatabase(message);
    }

    @Override
    public List<CameraMessage> emptyMemoryBuffer() {
        final List<CameraMessage> temp = cameraMessages;
        cameraMessages = new ArrayList<>();
        return temp;
    }

    @Override
    @Transactional
    public void bufferInDatabase(CameraMessage message) {
        cameraService.saveCameraWithSegment(message);
    }

    @Override
    public void bufferInMemory(CameraMessage message) {
        cameraMessages.add(message);
    }
}
