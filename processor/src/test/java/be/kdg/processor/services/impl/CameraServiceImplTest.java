package be.kdg.processor.services.impl;

import be.kdg.processor.config.converters.IoConverter;
import be.kdg.processor.config.dtos.CameraMessageDTO;
import be.kdg.processor.config.helpers.CameraMessageBuffer;
import be.kdg.processor.services.api.CameraService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CameraServiceImplTest {
    public static final String msg1 = "<CameraMessage><cameraId>4</cameraId><licensePlate>2-ABC-123</licensePlate><timestamp>2018-10-05T10:56:39.4985467</timestamp><delay>100</delay></CameraMessage>";
    public static final String msg2 = "<CameraMessage><cameraId>5</cameraId><licensePlate>2-ABC-123</licensePlate><timestamp>2018-10-05T10:56:41.5003778</timestamp><delay>2000</delay></CameraMessage>";
    @Autowired
    private CameraService cameraService;
    @Autowired
    private CameraMessageBuffer buffer;
    @Autowired
    private IoConverter ioConverter;

    @Test
    public void receiveCameraMessage() {
        String messageXml = "<CameraMessage><cameraId>1</cameraId><licensePlate>3-QBP-635</licensePlate><timestamp>2018-10-01T19:21:25.6820299</timestamp><delay>0</delay></CameraMessage>";
        cameraService.receiveCameraMessage(messageXml);
    }

    @Test
    public void emptyBuffer() {
        buffer.add(ioConverter.readXml(msg1, CameraMessageDTO.class));
        buffer.add(ioConverter.readXml(msg2, CameraMessageDTO.class));
        cameraService.emptyBuffer();
    }
}