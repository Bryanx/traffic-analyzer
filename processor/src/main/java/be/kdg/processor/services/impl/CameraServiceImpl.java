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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public void receiveCameraMessage(String msg) {
        LOGGER.info("received message: {}", msg);
        CameraMessageDTO xmldto = ioConverter.readXml(msg);
        CameraMessage message = new CameraMessage(xmldto.getLicensePlate(),xmldto.getTimestamp());

        String json = proxyCameraService.get(xmldto.getCameraId());
        JsonObject dto = ioConverter.readJson(json);
        CameraCouple couple = ioConverter.jsonToObjects(dto);
        if (couple == null) {
            Camera camera = new Camera(dto);
        }

        //Message comes in.
        //Get camera id from message and send to proxy, get camera details and couple details.
        //Store message in local buffer.
        //create camera en couple in db.
    }

    private void createCameraCoupleAndMessage(CameraCouple couple, Camera camera, CameraMessage message) {
        camera.addCameraMessage(message);
        if (couple != null) {
            couple.addCamera(camera);
            createCameraCouple(couple);
        } else {
            createCamera(camera);
        }
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
