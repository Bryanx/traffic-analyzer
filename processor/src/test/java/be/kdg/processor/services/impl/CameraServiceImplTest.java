package be.kdg.processor.services.impl;

import be.kdg.processor.ProcessorApplication;
import be.kdg.processor.services.api.CameraService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CameraServiceImplTest {

    @Autowired
    private CameraService cameraService;

    @Test
    public void receiveCameraMessage() {
        String messageXml = "<CameraMessage><cameraId>1</cameraId><licensePlate>3-QBP-635</licensePlate><timestamp>2018-10-01T19:21:25.6820299</timestamp><delay>0</delay></CameraMessage>";
        cameraService.receiveCameraMessage(messageXml);
    }
}