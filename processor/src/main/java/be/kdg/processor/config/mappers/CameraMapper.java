package be.kdg.processor.config.mappers;

import be.kdg.processor.config.converters.IoConverter;
import be.kdg.processor.config.dtos.CameraMessageDTO;
import be.kdg.processor.config.dtos.CameraProxyDTO;
import be.kdg.processor.domain.Camera;
import be.kdg.processor.domain.CameraCouple;
import be.kdg.processor.domain.CameraMessage;
import be.kdg.processor.services.ThirdParty.ProxyCameraService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class CameraMapper {
    private final ModelMapper modelMapper;
    private final ProxyCameraService proxyCameraService;
    private final IoConverter ioConverter;

    public CameraMessage msgDtoToMessage(CameraMessageDTO msgDto) {
        return modelMapper.map(msgDto, CameraMessage.class);
    }
    private CameraProxyDTO getDto(int cameraId) {
        String json = proxyCameraService.get(cameraId);
        return ioConverter.readJson(json, CameraProxyDTO.class);
    }

    public CameraCouple mapCameraCouple(CameraMessageDTO msg1, CameraMessageDTO msg2) {
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
