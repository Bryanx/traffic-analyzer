package be.kdg.processor.camera.proxy;

import be.kdg.processor.camera.Camera;
import be.kdg.processor.camera.message.CameraMessage;
import be.kdg.processor.camera.segment.Segment;
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
    public void getCamera() {
        CameraMessage msg = new CameraMessage();
        msg.setCameraId(5);
        msg.setLicensePlate("2-ABC-123");
        msg.setTimestamp(LocalDateTime.now());
        Camera result = proxyCameraService.fetchCamera(msg);
        System.out.println(result);
        Assert.assertNotNull(result);
    }

    @Test
    public void getSegment() {
        CameraMessage msg1 = new CameraMessage();
        msg1.setCameraId(5);
        msg1.setLicensePlate("2-ABC-123");
        msg1.setTimestamp(LocalDateTime.now());
        CameraMessage msg2 = new CameraMessage();
        msg2.setCameraId(4);
        msg2.setLicensePlate("2-ABC-123");
        msg2.setTimestamp(LocalDateTime.now());
        Segment result = proxyCameraService.fetchSegment(msg1,msg2);
        System.out.println(result);
        Assert.assertNotNull(result);
    }
}