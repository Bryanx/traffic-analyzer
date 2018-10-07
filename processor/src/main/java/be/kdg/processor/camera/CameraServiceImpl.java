package be.kdg.processor.camera;

import be.kdg.processor.camera.message.CameraMessage;
import be.kdg.processor.camera.message.CameraMessageBuffer;
import be.kdg.processor.camera.message.CameraMessageRepository;
import be.kdg.processor.camera.proxy.ProxyCameraService;
import be.kdg.processor.camera.segment.Segment;
import be.kdg.processor.fine.FineService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@AllArgsConstructor
@Service
@Transactional
public class CameraServiceImpl implements CameraService {
    private static final Logger LOGGER = LoggerFactory.getLogger(CameraServiceImpl.class);
    private final CameraMessageRepository cameraMessageRepository;
    private final CameraRepository cameraRepository;
    private final CameraMessageBuffer buffer;
    private final List<FineService> fineServices;
    private final ProxyCameraService proxyCameraService;

    @Override
    public CameraMessage createCameraMessage(CameraMessage message) {
        CameraMessage addedMsg = cameraMessageRepository.save(message);
        if (addedMsg != null) LOGGER.info("Added CameraMessage to DB: {}", addedMsg);
        return addedMsg;
    }

    @Override
    public void deleteCameraMessage(CameraMessage message) {
        cameraMessageRepository.delete(message);
    }

    @Override
    public Camera createOrUpdateCamera(Camera camera) {
        if (cameraRepository.findById(camera.getCameraId()).isPresent()) {
            return null;
        }
        Camera addedCamera = cameraRepository.save(camera);
        if (addedCamera != null) LOGGER.info("Added camera to DB: {}", addedCamera);
        return addedCamera;
    }

    @RabbitListener(queues = "camera-message-queue")
    @Override
    public void receiveCameraMessage(@Payload CameraMessage message) {
        LOGGER.info("Received message: {}", message);
        buffer.add(message);
    }

    @Scheduled(fixedDelayString = "${buffer.config.timebetween}000")
    public void emptyBuffer() {
        CameraMessageBuffer tempBuffer = (CameraMessageBuffer) buffer.clone();
        buffer.clear();
        tempBuffer.forEach(msg -> {
            CameraMessage poppedMsg = tempBuffer.getMessageWithSamePlate(msg);
            if (poppedMsg == null) return;
            Segment segment = proxyCameraService.fetchSegment(msg, poppedMsg);
            if (segment == null) return;
            fineServices.forEach(fineService -> fineService.checkForFine(segment));
        });
    }
}
