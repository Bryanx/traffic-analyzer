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
import be.kdg.processor.services.api.FineService;
import com.google.gson.JsonObject;
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
    private final FineService fineService;

    @RabbitHandler
    @Override
    public void receiveCameraMessage(String xmlMessage) {
        LOGGER.info("Received message: {}", xmlMessage);
        buffer.add(ioConverter.readXml(xmlMessage));
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

    private JsonObject getJsonObjectFromProxy(int cameraId) {
        String json = proxyCameraService.get(cameraId);
        return ioConverter.readJson(json);
    }

    private CameraCouple createCameraCoupleFromProxy(CameraMessageDTO msg1, CameraMessageDTO msg2) {
        JsonObject jsonObject = getJsonObjectFromProxy(msg1.getCameraId());
        JsonObject jsonObject2 = getJsonObjectFromProxy(msg2.getCameraId());
        if (!messagesAreInSameSegment(jsonObject, jsonObject2, msg1, msg2)) return null;
        CameraCouple couple = ioConverter.jsonToObjects(jsonObject);
        Camera camera1 = new Camera(jsonObject);
        Camera camera2 = new Camera(jsonObject2);
        camera1.addCameraMessage(new CameraMessage(msg1.getLicensePlate(), msg1.getTimestamp()));
        camera2.addCameraMessage(new CameraMessage(msg2.getLicensePlate(), msg2.getTimestamp()));
        couple.addCamera(camera1);
        couple.addCamera(camera2);
        return couple;
    }

    private boolean messagesAreInSameSegment(JsonObject jsonObject, JsonObject jsonObject2, CameraMessageDTO msg1, CameraMessageDTO msg2) {
        boolean sameSegment = false;
        if (jsonObject.getAsJsonObject("segment") != null) {
            sameSegment = jsonObject.getAsJsonObject("segment").get("connectedCameraId").getAsInt() == msg2.getCameraId();
        }
        if (jsonObject2.getAsJsonObject("segment") != null) {
            sameSegment = jsonObject2.getAsJsonObject("segment").get("connectedCameraId").getAsInt() == msg1.getCameraId();
        }
        return sameSegment;
    }

    @Scheduled(fixedDelayString = "${buffer.config.timebetween}000")
    public void emptyBuffer() {
        buffer.forEach(msg -> {
            CameraMessageDTO poppedMsg = buffer.popMessageWithSamePlate(msg);
            if (poppedMsg == null) return;
            CameraCouple couple = createCameraCoupleFromProxy(msg, poppedMsg);
            if (couple == null) return;
            fineService.checkForFine(couple, getCameraMessagesFromCouple(couple));
        });
        //clear remaining messages on the buffer:
//        buffer.clear();
    }

    private List<CameraMessage> getCameraMessagesFromCouple(CameraCouple couple) {
        List<CameraMessage> messages = new ArrayList<>();
        couple.getCameras().forEach(camera -> messages.addAll(camera.getCameraMessages()));
        return messages;
    }
}
