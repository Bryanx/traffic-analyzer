package be.kdg.processor.camera;

import be.kdg.processor.camera.message.CameraMessage;
import be.kdg.processor.camera.message.CameraMessageRepository;
import be.kdg.processor.camera.proxy.ProxyCameraService;
import be.kdg.processor.camera.segment.Segment;
import be.kdg.processor.fine.FineService;
import be.kdg.processor.shared.GeneralConfig;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@Service
@Transactional
public class CameraServiceImpl implements CameraService {
    private static final Logger LOGGER = LoggerFactory.getLogger(CameraServiceImpl.class);
    private final CameraMessageRepository cameraMessageRepository;
    private final CameraRepository cameraRepository;
    private final List<FineService> fineServices;
    private final ProxyCameraService proxyCameraService;
    private final GeneralConfig generalConfig;

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

    public List<CameraMessage> findAllSince(LocalDateTime since) {
        return cameraMessageRepository.findAllByTimestampBetween(since, LocalDateTime.now());
    }

    @RabbitListener(queues = "camera-message-queue")
    @Override
    public void receiveCameraMessage(@Payload CameraMessage message) {
        LOGGER.info("Received message: {}", message);
        createCameraMessage(message);
    }

    @Scheduled(fixedDelayString = "${buffer.config.time}000")
    public void emptyBuffer() {
        List<CameraMessage> cameraMessages = findAllSince(LocalDateTime.now().minusSeconds(generalConfig.getTime()));
        if (cameraMessages.size() == 0) return;
        LOGGER.info("Processing " + cameraMessages.size() + " buffered cameraMessages");

        CameraMessage matchedMessage = null;
        for (CameraMessage msg : cameraMessages) {
            if (matchedMessage == msg) return;
            for (CameraMessage otherMsg : cameraMessages) {
                if (msg != otherMsg &&
                        msg.getLicensePlate().equals(otherMsg.getLicensePlate()) &&
                        msg.getCameraId() != otherMsg.getCameraId()) {
                    LOGGER.info("Found 2 CameraMessages with the same licenseplate: " + msg + ", " + otherMsg);
                    Segment segment = proxyCameraService.fetchSegment(msg, otherMsg);
                    fineServices.forEach(fineService -> fineService.checkForFine(segment));
                    matchedMessage = otherMsg;
                }
            }
        }
    }
}
