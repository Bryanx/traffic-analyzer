package be.kdg.processor.camera;

import be.kdg.processor.camera.message.CameraMessage;
import be.kdg.processor.camera.segment.Segment;

public interface CameraService {

    void receiveCameraMessage(CameraMessage msg);

    Segment createSegment(Segment segment);

    Camera createCamera(Camera camera);

}