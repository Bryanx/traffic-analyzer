package be.kdg.processor.camera;

import be.kdg.processor.camera.couple.CameraCouple;
import be.kdg.processor.camera.message.CameraMessage;
import be.kdg.processor.camera.proxy.ProxyCameraService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class CameraMapper {
    private final ProxyCameraService proxyCameraService;

    public CameraCouple mapCameraCouple(CameraMessage msg1, CameraMessage msg2) {
        Camera camera1 = proxyCameraService.get(msg1.getCameraId());
        Camera camera2 = proxyCameraService.get(msg1.getCameraId());

        if (camera1.getSegment() == null) return null;
        CameraCouple couple;

        if (camera1.getSegment() != null) couple = new CameraCouple(camera1.getSegment().getSpeedLimit(), camera1.getSegment().getDistance());
        else couple = new CameraCouple(camera2.getSegment().getSpeedLimit(), camera2.getSegment().getDistance());

        camera1.addCameraMessage(new CameraMessage(msg1.getLicensePlate(), msg1.getTimestamp()));
        camera2.addCameraMessage(new CameraMessage(msg2.getLicensePlate(), msg2.getTimestamp()));
        couple.addCamera(camera1);
        couple.addCamera(camera2);

        return couple;
    }
}
