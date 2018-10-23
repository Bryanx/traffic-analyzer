package be.kdg.processor.camera.proxy;

import be.kdg.processor.camera.Camera;
import be.kdg.processor.camera.message.CameraMessage;
import be.kdg.processor.shared.exception.ProcessorException;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ProxyCameraServiceImplTest {

    //Add '--add-modules java.xml.bind' to VM options before running.
    @Autowired
    private ProxyCameraServiceImpl proxyCameraService;

    @Test
    public void getCamera() throws ProcessorException {
        CameraMessage msg = new CameraMessage();
        msg.setCameraId(5);
        msg.setLicensePlate("2-ABC-123");
        msg.setTimestamp(LocalDateTime.now());
        Camera result = proxyCameraService.fetchCamera(msg);
        System.out.println(result);
        Assert.assertNotNull(result);
    }
}