package be.kdg.processor.camera;

import be.kdg.processor.camera.message.CameraMessage;
import be.kdg.processor.camera.message.CameraMessageDTO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CameraMapperTest {
    @Autowired
    private CameraMapper cameraMessageMapper;

    @Test
    public void convertToEntity() {
        CameraMessageDTO dto = new CameraMessageDTO();
        dto.setDelay(500);
        dto.setLicensePlate("234324");
        dto.setTimestamp(LocalDateTime.of(1900,1,1,1,1,1));
        CameraMessage msg = cameraMessageMapper.msgDtoToMessage(dto);
        System.out.println(dto);
        System.out.println(msg);
        assertEquals(msg.getLicensePlate(), dto.getLicensePlate());
        assertEquals(msg.getTimestamp(), dto.getTimestamp());
    }

}