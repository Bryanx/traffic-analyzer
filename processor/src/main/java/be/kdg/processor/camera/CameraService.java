package be.kdg.processor.camera;

import be.kdg.processor.camera.message.CameraMessage;
import be.kdg.processor.camera.segment.Segment;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface CameraService {

    Optional<Segment> createSegment(Segment segment);

    Camera createCamera(Camera camera);

    Optional<List<CameraMessage>> findAllCameraMessagesSince(LocalDateTime since);

    void saveCameraWithSegment(CameraMessage message);
}