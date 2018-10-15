package be.kdg.processor.camera.message;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CameraMessageRepositoryTest {

    @Autowired
    private CameraMessageRepository cameraMessageRepository;

    @Test
    public void save() {
        CameraMessage msg1 = new CameraMessage();
        msg1.setCameraId(5);
        msg1.setLicensePlate("2-ABC-123");
        msg1.setTimestamp(LocalDateTime.now());
        CameraMessage savedMessage = cameraMessageRepository.save(msg1);
        assertNotNull(savedMessage);
    }

    @Test
    public void findAllByTimestampBetween() {
        CameraMessage msg1 = new CameraMessage();
        msg1.setCameraId(5);
        msg1.setLicensePlate("2-ABC-123");
        msg1.setTimestamp(LocalDateTime.now());
        cameraMessageRepository.save(msg1);
        Optional<List<CameraMessage>> allByTimestampBetween = cameraMessageRepository.findAllByTimestampBetween(LocalDateTime.now().minusSeconds(200), LocalDateTime.now());
        System.out.println(allByTimestampBetween.get());
        assertEquals(allByTimestampBetween.get().size(), 1);
    }
}