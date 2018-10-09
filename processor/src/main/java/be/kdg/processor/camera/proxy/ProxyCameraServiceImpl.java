package be.kdg.processor.camera.proxy;

import be.kdg.processor.camera.Camera;
import be.kdg.processor.camera.message.CameraMessage;
import be.kdg.processor.camera.segment.Segment;
import be.kdg.processor.shared.converters.IoConverter;
import be.kdg.sa.services.CameraNotFoundException;
import be.kdg.sa.services.CameraServiceProxy;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Arrays;

@RequiredArgsConstructor
@Component
public class ProxyCameraServiceImpl implements ProxyCameraService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProxyCameraServiceImpl.class);
    private final CameraServiceProxy cameraServiceProxy;
    private final IoConverter ioConverter;

    //TODO: Add cache
    @Override
    public Camera fetchCamera(CameraMessage message) {
        try {
            String json = cameraServiceProxy.get(message.getCameraId());
            Camera camera = ioConverter.readJson(json, Camera.class);
            camera.addCameraMessage(message);
            return camera;
        } catch (IOException e) {
            LOGGER.error("Camera with id {} forced a communication error.", message.getCameraId());
        } catch (CameraNotFoundException e) {
            LOGGER.warn("Camera with id {} not found.", message.getCameraId());
        }
        return null;
    }

    /**
     * Gets the cameras for both messages.
     * If one of the cameras is not included in the segment of the other message it is added here.
     *
     * @return the segment containing both messages.
     */
    @Override
    public Segment fetchSegment(CameraMessage message1, CameraMessage message2) {
        for (CameraMessage cameraMessage : Arrays.asList(message1, message2)) {
            Segment segment = cameraMessage.getCamera().getSegment();
            if (segment != null) {
                segment.addCamera(message1.getCamera());
                segment.addCamera(message2.getCamera());
                return segment;
            }
        }
        return null;
    }

}
