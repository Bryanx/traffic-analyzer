package be.kdg.processor.fine.evaluation;

import be.kdg.processor.camera.message.CameraMessage;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SpeedFineServiceTest {

    @Autowired
    private SpeedFineService speedFineService;

    @Test
    public void testCalculateSpeed() {
        CameraMessage msg1 = new CameraMessage("2-ABC-123", LocalDateTime.now().minusHours(1));
        CameraMessage msg2 = new CameraMessage("2-ABC-123", LocalDateTime.now());
        double speedTest1 = speedFineService.calculateSpeed(50000, msg1, msg2);
        double speedTest2 = speedFineService.calculateSpeed(50000, msg2, msg1);
        assertEquals(50, speedTest1, 0.1);
        assertEquals(50, speedTest2, 0.1);
    }

}