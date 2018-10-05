package be.kdg.processor.services.impl;

import be.kdg.processor.config.converters.IoConverter;
import be.kdg.processor.config.dtos.CameraMessageDTO;
import be.kdg.processor.config.dtos.CameraProxyDTO;
import be.kdg.processor.config.helpers.CameraMessageBuffer;
import be.kdg.processor.domain.Camera;
import be.kdg.processor.domain.CameraCouple;
import be.kdg.processor.domain.CameraMessage;
import be.kdg.processor.persistence.api.CameraCoupleRepository;
import be.kdg.processor.persistence.api.CameraMessageRepository;
import be.kdg.processor.persistence.api.CameraRepository;
import be.kdg.processor.services.ThirdParty.ProxyCameraService;
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
    private final ProxyCameraService proxyCameraService;
    private final CameraMessageRepository cameraMessageRepository;
    private final CameraRepository cameraRepository;
    private final CameraCoupleRepository cameraCoupleRepository;
    private final CameraMessageBuffer buffer;
    private final List<FineService> fineServices;

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

    //TODO: Use different method than jsonobjects to extract data.
    private CameraProxyDTO getDto(int cameraId) {
        String json = proxyCameraService.get(cameraId);
        return ioConverter.jsonToObject(json, CameraProxyDTO.class);
    }

    private CameraCouple createCameraCoupleFromProxy(CameraMessageDTO msg1, CameraMessageDTO msg2) {
        CameraProxyDTO dto1 = getDto(msg1.getCameraId());
        CameraProxyDTO dto2 = getDto(msg2.getCameraId());

        if (!dto1.isInSameSegment(dto2)) return null;

//        CameraCouple couple = ioConverter.jsonToObjects(jsonObject);
//        if (couple == null) couple = ioConverter.jsonToObjects(jsonObject2);
//        Camera camera1 = new Camera(jsonObject);
//        Camera camera2 = new Camera(jsonObject2);
//        camera1.addCameraMessage(new CameraMessage(msg1.getLicensePlate(), msg1.getTimestamp()));
//        camera2.addCameraMessage(new CameraMessage(msg2.getLicensePlate(), msg2.getTimestamp()));
//        couple.addCamera(camera1);
//        couple.addCamera(camera2);
        return null;
    }

    @Scheduled(fixedDelayString = "${buffer.config.timebetween}000")
    public void emptyBuffer() {
        CameraMessageBuffer tempBuffer = buffer;
        buffer.clear();
        tempBuffer.forEach(msg -> {
            CameraMessageDTO poppedMsg = tempBuffer.popMessageWithSamePlate(msg);
            if (poppedMsg == null) return;
            CameraCouple couple = createCameraCoupleFromProxy(msg, poppedMsg);
            if (couple == null) return;
            fineServices.forEach(fineService -> fineService.checkForFine(couple));
        });
    }
}
