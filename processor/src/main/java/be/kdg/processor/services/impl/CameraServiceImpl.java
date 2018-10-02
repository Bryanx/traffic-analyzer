package be.kdg.processor.services.impl;

import be.kdg.processor.config.converters.IoConverter;
import be.kdg.processor.config.helpers.CameraMessageDTO;
import be.kdg.processor.domain.Camera;
import be.kdg.processor.domain.CameraCouple;
import be.kdg.processor.domain.CameraMessage;
import be.kdg.processor.persistence.api.CameraCoupleRepository;
import be.kdg.processor.persistence.api.CameraMessageRepository;
import be.kdg.processor.persistence.api.CameraRepository;
import be.kdg.processor.services.ThirdParty.ProxyCameraService;
import be.kdg.processor.services.api.CameraService;
import com.google.gson.JsonObject;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;

@AllArgsConstructor
@Service("cameraMessageService")
@RabbitListener(queues = "camera-message-queue")
@Transactional
public class CameraServiceImpl implements CameraService {
    private static final Logger LOGGER = LoggerFactory.getLogger(CameraServiceImpl.class);
    private final IoConverter ioConverter;
    private final ProxyCameraService cameraService;
    private final CameraMessageRepository cameraMessageRepository;
    private final CameraRepository cameraRepository;
    private final CameraCoupleRepository cameraCoupleRepository;

    @RabbitHandler
    public void receiveCameraMessage(String msg) {
        LOGGER.info("received message: {}", msg);
        CameraMessageDTO xmldto = ioConverter.readXml(msg);
        CameraMessage message = new CameraMessage(xmldto.getLicensePlate(),xmldto.getTimestamp());
        String json = cameraService.get(xmldto.getCameraId());

        final JsonObject dto = ioConverter.readJson(json);
        CameraCouple couple = new CameraCouple(dto);
        Camera camera = new Camera(dto);
        camera.addCameraMessage(message);
        couple.addCamera(camera);
        createCameraCouple(couple);
    }

    @Override
    public CameraMessage createCameraMessage(CameraMessage message) {
        return cameraMessageRepository.save(message);
    }

    @Override
    public void deleteCameraMessage(CameraMessage message) {
        cameraMessageRepository.delete(message);
    }

    @Override
    public Camera createCamera(Camera camera) {
        return cameraRepository.save(camera);
    }

    @Override
    public CameraCouple createCameraCouple(CameraCouple couple) {
        return cameraCoupleRepository.save(couple);
    }
}
