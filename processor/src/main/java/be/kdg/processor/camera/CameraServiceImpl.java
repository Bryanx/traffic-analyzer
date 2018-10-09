package be.kdg.processor.camera;

import be.kdg.processor.camera.message.CameraMessage;
import be.kdg.processor.camera.message.CameraMessageRepository;
import be.kdg.processor.camera.proxy.ProxyCameraService;
import be.kdg.processor.camera.segment.Segment;
import be.kdg.processor.camera.segment.SegmentRepository;
import be.kdg.processor.fine.FineService;
import be.kdg.processor.shared.GeneralConfig;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@RequiredArgsConstructor
@Service
@Transactional
public class CameraServiceImpl implements CameraService {
    private static final Logger LOGGER = LoggerFactory.getLogger(CameraServiceImpl.class);
    private final CameraMessageRepository cameraMessageRepository;
    private final CameraRepository cameraRepository;
    private final SegmentRepository segmentRepository;
    private final List<FineService> fineServices;
    private final ProxyCameraService proxyCameraService;
    private final GeneralConfig generalConfig;

    @Override
    public CameraMessage createCameraMessage(CameraMessage message) {
        CameraMessage addedMsg = cameraMessageRepository.saveAndFlush(message);
        if (addedMsg != null) LOGGER.info("Added CameraMessage to DB: {}", addedMsg);
        return addedMsg;
    }

    @Override
    public Camera createCamera(Camera camera) {
        if (camera != null) LOGGER.info("Added camera to DB: {}", camera);
        return cameraRepository.saveAndFlush(camera);
    }

    @Override
    public Segment createSegment(Segment segment) {
        LOGGER.info("Adding segment to DB: " + segment);
        return segmentRepository.saveAndFlush(segment);
    }

    public List<CameraMessage> findAllCameraMessagesSince(LocalDateTime since) {
        return cameraMessageRepository.findAllByTimestampBetween(since, LocalDateTime.now());
    }

    @RabbitListener(queues = "camera-message-queue")
    @Override
    public void receiveCameraMessage(@Payload CameraMessage cameraMessageIn) {
        LOGGER.info("Received message: {}", cameraMessageIn);
        Camera camera = proxyCameraService.fetchCamera(cameraMessageIn);
        if (camera == null) {
            return;
        }
        if (camera.getSegment() != null) {
            Segment segment = createSegment(camera.getSegment());
            camera.setSegment(segment);
            createCamera(camera);
        } else {
            createCamera(camera);
        }
    }

    @Scheduled(fixedDelayString = "${buffer.config.time}000")
    public void emptyBuffer() {
        LocalDateTime bufferTime = LocalDateTime.now().minusSeconds(generalConfig.getBufferTime());
//        LocalDateTime bufferTime = LocalDateTime.now().minusSeconds(600000000);
        List<CameraMessage> cameraMessages = findAllCameraMessagesSince(bufferTime);
        if (cameraMessages.size() == 0) return;
        LOGGER.info("Processing " + cameraMessages.size() + " buffered cameraMessages");

        CameraMessage matchedMessage = null;
        for (CameraMessage msg : cameraMessages) {
            if (matchedMessage == msg) return;
            for (CameraMessage otherMsg : cameraMessages) {
                if (msg != otherMsg &&
                        msg.getCamera().getSegment() != null &&
                        msg.getCamera().getSegment().getConnectedCameraId() == otherMsg.getCameraId() &&
                        msg.getLicensePlate().equals(otherMsg.getLicensePlate())) {
                    LOGGER.info("Found 2 CameraMessages with the same licenseplate: " + msg + ", " + otherMsg);
                    fineServices.forEach(fineService -> {
                        fineService.checkForFine(Arrays.asList(msg, otherMsg));
                    });
                    matchedMessage = otherMsg;
                }
            }
        }
    }
}
