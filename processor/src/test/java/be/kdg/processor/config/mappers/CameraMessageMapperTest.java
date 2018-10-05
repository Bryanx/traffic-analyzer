package be.kdg.processor.config.mappers;

import be.kdg.processor.config.dtos.CameraMessageDTO;
import be.kdg.processor.domain.CameraMessage;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CameraMessageMapperTest {

    @Autowired
    private CameraMessageMapper cameraMessageMapper;

    @Test
    public void convertToEntity() {
        CameraMessageDTO dto = new CameraMessageDTO();
        dto.setDelay(500);
        dto.setLicensePlate("234324");
        dto.setTimestamp(LocalDateTime.of(1900,1,1,1,1,1));
        CameraMessage msg = cameraMessageMapper.convertToEntity(dto);
        System.out.println(dto);
        System.out.println(msg);
        assertEquals(msg.getLicensePlate(), dto.getLicensePlate());
        assertEquals(msg.getTimestamp(), dto.getTimestamp());
    }
}