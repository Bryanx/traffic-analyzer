package be.kdg.processor.services.impl;

import be.kdg.processor.config.converters.IoConverter;
import be.kdg.processor.config.helpers.CameraMessageBuffer;
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
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@AllArgsConstructor
@Service("cameraMessageService")
@RabbitListener(queues = "camera-message-queue")
@Transactional
public class CameraServiceImpl implements CameraService {
    private static final Logger LOGGER = LoggerFactory.getLogger(CameraServiceImpl.class);
    private final IoConverter ioConverter;
    private final ProxyCameraService proxyCameraService;
    private final CameraMessageRepository cameraMessageRepository;
    private final CameraRepository cameraRepository;
    private final CameraCoupleRepository cameraCoupleRepository;
    private final CameraMessageBuffer buffer;

    @RabbitHandler
    @Override
    public void receiveCameraMessage(String xmlMessage) {
        LOGGER.info("Received message: {}", xmlMessage);
        CameraMessageDTO dtoMessage = ioConverter.readXml(xmlMessage);
        buffer.add(new CameraMessage(dtoMessage.getLicensePlate(),dtoMessage.getTimestamp()));
        createCameraCoupleFromProxy(dtoMessage.getCameraId());
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

    private void createCameraCoupleFromProxy(int cameraId) {
        String json = proxyCameraService.get(cameraId);
        JsonObject jsonObject = ioConverter.readJson(json);
        CameraCouple couple = ioConverter.jsonToObjects(jsonObject);
        if (couple == null) {
            createCamera(new Camera(jsonObject));
        } else {
            createCameraCouple(couple);
        }
    }

    @Scheduled(fixedDelayString = "${buffer.config.timebetween}")
    public void emptyBuffer() {
        List<CameraMessage> messages = buffer.empty();
        LOGGER.info("Took " + messages.size() + " messages from the buffer.");
    }
}
