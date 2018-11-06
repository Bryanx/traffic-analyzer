package be.kdg.processor.camera;

import be.kdg.processor.camera.message.CameraMessage;
import be.kdg.processor.camera.segment.Segment;
import be.kdg.processor.shared.exception.ProcessorException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface CameraService {

    Optional<Segment> createSegment(Segment segment);

    Segment findSegmentByCameraMessage(CameraMessage message) throws ProcessorException;

    Camera createCamera(Camera camera);

    List<CameraMessage> findAllCameraMessages();

    Optional<List<CameraMessage>> findAllCameraMessagesSince(LocalDateTime since);

    void saveCameraWithSegment(CameraMessage message) throws ProcessorException;

    CameraMessage getConnectedCameraMessage(Segment segment, CameraMessage cameraMessage) throws ProcessorException;

}