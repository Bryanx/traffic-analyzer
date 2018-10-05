package be.kdg.processor.services.impl;

import be.kdg.processor.config.converters.IoConverter;
import be.kdg.processor.config.dtos.CameraMessageDTO;
import be.kdg.processor.config.helpers.CameraMessageBuffer;
import be.kdg.processor.config.mappers.CameraMapper;
import be.kdg.processor.domain.Camera;
import be.kdg.processor.domain.CameraCouple;
import be.kdg.processor.domain.CameraMessage;
import be.kdg.processor.persistence.api.CameraCoupleRepository;
import be.kdg.processor.persistence.api.CameraMessageRepository;
import be.kdg.processor.persistence.api.CameraRepository;
import be.kdg.processor.services.api.CameraService;
import be.kdg.processor.services.api.FineService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Service("cameraMessageService")
@RabbitListener(queues = "camera-message-queue")
@Transactional
public class CameraServiceImpl implements CameraService {
    private static final Logger LOGGER = LoggerFactory.getLogger(CameraServiceImpl.class);
    private final IoConverter ioConverter;
    private final CameraMessageRepository cameraMessageRepository;
    private final CameraRepository cameraRepository;
    private final CameraCoupleRepository cameraCoupleRepository;
    private final CameraMessageBuffer buffer;
    private final List<FineService> fineServices;
    private final CameraMapper cameraMapper;

    @RabbitHandler
    @Override
    public void receiveCameraMessage(String xmlMessage) {
        LOGGER.info("Received message: {}", xmlMessage);
        buffer.add(ioConverter.readXml(xmlMessage, CameraMessageDTO.class));
    }

    @Override
    public CameraMessage createCameraMessage(CameraMessage message) {
        CameraMessage addedMsg = cameraMessageRepository.save(message);
        if (addedMsg != null) LOGGER.info("Added CameraMessage to DB: {}", addedMsg);
        return addedMsg;
    }

    @Override
    public void deleteCameraMessage(CameraMessage message) {
        cameraMessageRepository.delete(message);
    }

    @Override
    public Camera createOrUpdateCamera(Camera camera) {
        if (cameraRepository.findById(camera.getId()).isPresent()) {
            return null;
        }
        Camera addedCamera = cameraRepository.save(camera);
        if (addedCamera != null) LOGGER.info("Added camera to DB: {}", addedCamera);
        return addedCamera;
    }

    @Override
    public CameraCouple createOrUpdateCameraCouple(CameraCouple couple) {
        if (cameraCoupleRepository.findById(couple.getId()).isPresent()) {
            return null;
        }
        CameraCouple addedCouple = cameraCoupleRepository.save(couple);
        if (addedCouple != null) LOGGER.info("Added CameraCouple to DB: {}", addedCouple);
        return addedCouple;
    }

    @Override
    public List<CameraMessage> getCameraMessagesFromCouple(CameraCouple couple) {
        List<CameraMessage> messages = new ArrayList<>();
        couple.getCameras().forEach(camera -> messages.addAll(camera.getCameraMessages()));
        return messages;
    }

    @Scheduled(fixedDelayString = "${buffer.config.timebetween}000")
    public void emptyBuffer() {
        CameraMessageBuffer tempBuffer = (CameraMessageBuffer) buffer.clone();
        buffer.clear();
        tempBuffer.forEach(msg -> {
            CameraMessageDTO poppedMsg = tempBuffer.popMessageWithSamePlate(msg);
            if (poppedMsg == null) return;
            CameraCouple couple = cameraMapper.mapCameraCouple(msg, poppedMsg);
            if (couple == null) return;
            fineServices.forEach(fineService -> fineService.checkForFine(couple));
        });
    }
}
