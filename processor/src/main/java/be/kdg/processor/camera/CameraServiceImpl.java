package be.kdg.processor.camera;

import be.kdg.processor.camera.couple.CameraCouple;
import be.kdg.processor.camera.couple.CameraCoupleRepository;
import be.kdg.processor.camera.message.CameraMessage;
import be.kdg.processor.camera.message.CameraMessageBuffer;
import be.kdg.processor.camera.message.CameraMessageDTO;
import be.kdg.processor.camera.message.CameraMessageRepository;
import be.kdg.processor.fine.FineService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Service
@Transactional
public class CameraServiceImpl implements CameraService {
    private static final Logger LOGGER = LoggerFactory.getLogger(CameraServiceImpl.class);
    private final CameraMessageRepository cameraMessageRepository;
    private final CameraRepository cameraRepository;
    private final CameraCoupleRepository cameraCoupleRepository;
    private final CameraMessageBuffer buffer;
    private final List<FineService> fineServices;
    private final CameraMapper cameraMapper;

    @RabbitListener(queues = "camera-message-queue")
    @Override
    public void receiveCameraMessage(@Payload CameraMessageDTO cameraMessageDTO) {
        LOGGER.info("Received message: {}", cameraMessageDTO);
//        buffer.add(ioConverter.readXml(xmlMessage, CameraMessageDTO.class));
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
            CameraMessageDTO poppedMsg = tempBuffer.getMessageWithSamePlate(msg);
            if (poppedMsg == null) return;
            CameraCouple couple = cameraMapper.mapCameraCouple(msg, poppedMsg);
            if (couple == null) return;
            fineServices.forEach(fineService -> fineService.checkForFine(couple));
        });
    }
}
