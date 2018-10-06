package be.kdg.processor.camera;

import be.kdg.processor.camera.couple.CameraCouple;
import be.kdg.processor.camera.message.CameraMessage;
import be.kdg.processor.camera.proxy.CameraProxyDTO;
import be.kdg.processor.camera.proxy.ProxyCameraService;
import be.kdg.processor.shared.converters.IoConverter;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class CameraMapper {
    private final ModelMapper modelMapper;
    private final ProxyCameraService proxyCameraService;
    private final IoConverter ioConverter;

    public CameraMessage msgDtoToMessage(CameraMessage msgDto) {
        return modelMapper.map(msgDto, CameraMessage.class);
    }

    private CameraProxyDTO getDto(int cameraId) {
        String json = proxyCameraService.get(cameraId);
        return ioConverter.readJson(json, CameraProxyDTO.class);
    }

    public CameraCouple mapCameraCouple(CameraMessage msg1, CameraMessage msg2) {
        CameraProxyDTO dto1 = getDto(msg1.getCameraId());
        CameraProxyDTO dto2 = getDto(msg2.getCameraId());

        if (!dto1.isInSameSegment(dto2)) return null;
        CameraCouple couple;

        if (dto1.getSegment() != null) couple = new CameraCouple(dto1.getSegment().getSpeedLimit(), dto1.getSegment().getDistance());
        else couple = new CameraCouple(dto2.getSegment().getSpeedLimit(), dto2.getSegment().getDistance());

        Camera camera1 = modelMapper.map(dto1, Camera.class);
        Camera camera2 = modelMapper.map(dto2, Camera.class);

        camera1.addCameraMessage(new CameraMessage(msg1.getLicensePlate(), msg1.getTimestamp()));
        camera2.addCameraMessage(new CameraMessage(msg2.getLicensePlate(), msg2.getTimestamp()));
        couple.addCamera(camera1);
        couple.addCamera(camera2);

        return couple;
    }
}
