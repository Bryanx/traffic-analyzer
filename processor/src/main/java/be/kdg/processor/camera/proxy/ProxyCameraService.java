package be.kdg.processor.camera.proxy;

import be.kdg.processor.camera.Camera;
import be.kdg.processor.camera.message.CameraMessage;
import be.kdg.processor.camera.segment.Segment;

public interface ProxyCameraService {
    Camera fetchCamera(CameraMessage message);
    Segment fetchSegment(CameraMessage message1, CameraMessage message2);
}
