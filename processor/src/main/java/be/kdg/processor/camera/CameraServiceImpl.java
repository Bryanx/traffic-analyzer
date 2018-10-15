package be.kdg.processor.camera;

import be.kdg.processor.camera.message.CameraMessage;
import be.kdg.processor.camera.message.CameraMessageRepository;
import be.kdg.processor.camera.proxy.ProxyCameraService;
import be.kdg.processor.camera.segment.Segment;
import be.kdg.processor.camera.segment.SegmentRepository;
import be.kdg.processor.fine.evaluation.FineEvaluationService;
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
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
@Transactional
public class CameraServiceImpl implements CameraService {
    private static final Logger LOGGER = LoggerFactory.getLogger(CameraServiceImpl.class);
    private final CameraMessageRepository cameraMessageRepository;
    private final CameraRepository cameraRepository;
    private final SegmentRepository segmentRepository;
    private final List<FineEvaluationService> fineEvaluationServices;
    private final ProxyCameraService proxyCameraService;
    private final GeneralConfig generalConfig;

    @Override
    public Camera createCamera(Camera camera) {
        if (camera != null) LOGGER.debug("Added camera to DB: {}", camera);
        return cameraRepository.saveAndFlush(camera);
    }

    @Override
    public Optional<Segment> createSegment(Segment segment) {
        LOGGER.debug("Adding segment to DB: " + segment);
        return Optional.ofNullable(segmentRepository.saveAndFlush(segment));
    }

    public List<CameraMessage> findAllCameraMessagesSince(LocalDateTime since) {
        return cameraMessageRepository.findAllByTimestampBetween(since, LocalDateTime.now());
    }

    @RabbitListener(queues = "camera-message-queue")
    @Override
    public void receiveCameraMessage(@Payload CameraMessage cameraMessageIn) {
        LOGGER.info("Received message: {}", cameraMessageIn);
        proxyCameraService.fetchCamera(cameraMessageIn).ifPresent(camera -> {
            if (camera.getSegment() != null) {
                createSegment(camera.getSegment())
                        .ifPresent(camera::setSegment);
            } else {
                segmentRepository.findSegmentByConnectedCameraId(camera.getCameraId())
                        .ifPresent(camera::setSegment);
            }
            createCamera(camera);
        });
    }

    @Scheduled(fixedDelayString = "${buffer.config.time}000")
    public void emptyBuffer() {
        LocalDateTime bufferTime = LocalDateTime.now().minusSeconds(generalConfig.getBufferTime());
//        LocalDateTime bufferTime = LocalDateTime.now().minusSeconds(600000000);
        List<CameraMessage> cameraMessages = findAllCameraMessagesSince(bufferTime);
        if (cameraMessages.size() == 0) return;
        LOGGER.info("Processing " + cameraMessages.size() + " buffered cameraMessages");

        for (CameraMessage cameraMessage : cameraMessages) {
            fineEvaluationServices.forEach(evaluationService -> evaluationService.checkForFine(cameraMessage));
        }
    }
}
