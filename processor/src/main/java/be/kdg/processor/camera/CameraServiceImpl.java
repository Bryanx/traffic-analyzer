package be.kdg.processor.camera;

import be.kdg.processor.camera.message.CameraMessage;
import be.kdg.processor.camera.message.CameraMessageRepository;
import be.kdg.processor.camera.proxy.ProxyCameraService;
import be.kdg.processor.camera.segment.Segment;
import be.kdg.processor.camera.segment.SegmentRepository;
import be.kdg.processor.shared.exception.ProcessorException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Service for crud related to CameraMessages, Cameras and Segments.
 */
@RequiredArgsConstructor
@Service
public class CameraServiceImpl implements CameraService {
    private static final Logger LOGGER = LoggerFactory.getLogger(CameraServiceImpl.class);
    private final CameraMessageRepository cameraMessageRepository;
    private final CameraRepository cameraRepository;
    private final SegmentRepository segmentRepository;
    private final ProxyCameraService proxyCameraService;

    @Override
    public Camera createCamera(Camera camera) {
        LOGGER.debug("Adding camera to DB: {}", camera);
        return cameraRepository.saveAndFlush(camera);
    }

    @Override
    public List<CameraMessage> findAllCameraMessages() {
        return cameraMessageRepository.findAll();
    }

    @Override
    public Optional<Segment> createSegment(Segment segment) {
        LOGGER.debug("Adding segment to DB: " + segment);
        return Optional.ofNullable(segmentRepository.saveAndFlush(segment));
    }

    @Override
    public Segment findSegmentByCameraMessage(CameraMessage message) throws ProcessorException {
        Segment segment = message.getCamera().getSegment();
        if (segment == null ) throw new ProcessorException("Segment not found for Camera message: " + message);
        return segment;
    }

    @Override
    public Optional<List<CameraMessage>> findAllCameraMessagesSince(LocalDateTime since) {
        return cameraMessageRepository.findAllByTimestampBetween(since, LocalDateTime.now());
    }

    @Override
    public void saveCameraWithSegment(CameraMessage message) throws ProcessorException {
        Camera camera = proxyCameraService.fetchCamera(message);
        if (camera.getSegment() != null) {
            createSegment(camera.getSegment()).ifPresent(camera::setSegment);
        } else {
            segmentRepository.findFirstByConnectedCameraId(camera.getCameraId()).ifPresent(camera::setSegment);
        }
        createCamera(camera);
    }


    /**
     * For a given segment and cameramessage, finds the corresponding cameramessage.
     */
    @Override
    public CameraMessage getConnectedCameraMessage(Segment segment, CameraMessage cameraMessage) throws ProcessorException {
        return segment.getCameras().stream()
                .filter(camera -> camera.getCameraId() == segment.getConnectedCameraId())
                .flatMap(c -> c.getCameraMessages().stream())
                .filter(message -> cameraMessage != message &&
                        cameraMessage.getLicensePlate().equals(message.getLicensePlate()))
                .findFirst()
                .orElseThrow(() -> new ProcessorException("Could not find connected camera message. For: " + cameraMessage));
    }
}
