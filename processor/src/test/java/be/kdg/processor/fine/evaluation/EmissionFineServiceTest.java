package be.kdg.processor.fine.evaluation;

import be.kdg.processor.camera.message.CameraMessage;
import be.kdg.processor.fine.Fine;
import be.kdg.processor.fine.FineType;
import be.kdg.processor.vehicle.Vehicle;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.Arrays;

import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest
public class EmissionFineServiceTest {

    @Autowired
    private EmissionFineService emissionFineService;

    @Test
    public void alreadyFined() {
        Vehicle vehicle = new Vehicle();
        vehicle.setPlateId("123-ABC-2");
        vehicle.setEuroNumber(2);
        CameraMessage msg1 = new CameraMessage("2-ABC-123", LocalDateTime.now().minusHours(1));
        CameraMessage msg2 = new CameraMessage("2-ABC-123", LocalDateTime.now());
        emissionFineService.createFine(new Fine(FineType.EMISSION, 150, 4, 2),
                vehicle, Arrays.asList(msg1, msg2));
        assertTrue(emissionFineService.alreadyFined(vehicle));
    }
}