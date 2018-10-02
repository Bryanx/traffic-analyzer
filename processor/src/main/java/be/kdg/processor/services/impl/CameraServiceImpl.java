package be.kdg.processor.services.impl;

import be.kdg.processor.config.converters.XmlConverter;
import be.kdg.processor.domain.CameraMessage;
import be.kdg.processor.persistence.api.CameraMessageRepository;
import be.kdg.processor.services.ThirdParty.ProxyCameraService;
import be.kdg.processor.services.api.CameraService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@AllArgsConstructor
@Service("cameraMessageService")
@RabbitListener(queues = "camera-message-queue")
@Transactional
public class CameraServiceImpl implements CameraService {
    private static final Logger LOGGER = LoggerFactory.getLogger(CameraServiceImpl.class);
    private final XmlConverter xmlConverter;
    private final ProxyCameraService cameraService;
    private final CameraMessageRepository cameraMessageRepository;

    @RabbitHandler
    public void receiveCameraMessage(String msg) {
        LOGGER.info("received message: {}", msg);
        CameraMessage message = xmlConverter.unmarshal(msg);
        createCameraMessage(message);
        cameraService.get(message.getCameraId());
        //id, localtionLat, locationLong, secondCamera, distance, speed, euroNorm,
        System.out.println("Received: " + message);
    }

    @Override
    public CameraMessage createCameraMessage(CameraMessage message) {
        return cameraMessageRepository.save(message);
    }

    @Override
    public void deleteCameraMessage(CameraMessage message) {
        cameraMessageRepository.delete(message);
    }
}
