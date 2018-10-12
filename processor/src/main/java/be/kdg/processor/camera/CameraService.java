package be.kdg.processor.camera;

import be.kdg.processor.camera.message.CameraMessage;
import be.kdg.processor.camera.segment.Segment;

import java.util.Optional;

public interface CameraService {

    void receiveCameraMessage(CameraMessage msg);

    Optional<Segment> createSegment(Segment segment);

    Camera createCamera(Camera camera);

}