package be.kdg.processor.fine.evaluation;

import be.kdg.processor.camera.CameraService;
import be.kdg.processor.camera.message.CameraMessage;
import be.kdg.processor.fine.Fine;
import be.kdg.processor.fine.FineService;
import be.kdg.processor.fine.FineType;
import be.kdg.processor.vehicle.Vehicle;
import be.kdg.processor.vehicle.VehicleService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SpeedFineServiceTest {

    @Autowired
    private SpeedFineService speedFineService;

    @Autowired
    private CameraService cameraService;

    @Autowired
    private FineService fineService;

    @Autowired
    private VehicleService vehicleService;

    @Test
    public void calculateSpeed() {
        CameraMessage msg1 = new CameraMessage("2-ABC-123", LocalDateTime.now().minusHours(1));
        CameraMessage msg2 = new CameraMessage("2-ABC-123", LocalDateTime.now());
        double speedTest1 = speedFineService.calculateSpeed(50000, msg1, msg2);
        double speedTest2 = speedFineService.calculateSpeed(50000, msg2, msg1);
        assertEquals(50, speedTest1, 0.1);
        assertEquals(50, speedTest2, 0.1);
    }

    @Test
    public void checkForFine() {
        CameraMessage message1 = new CameraMessage("4-ABC-123", LocalDateTime.now().minusSeconds(1));
        CameraMessage message2 = new CameraMessage("4-ABC-123", LocalDateTime.now());
        message1.setCameraId(1);
        message2.setCameraId(2);
        cameraService.saveCameraWithSegment(message1);
        cameraService.saveCameraWithSegment(message2);
        Optional<List<CameraMessage>> allCameraMessagesSince = cameraService.findAllCameraMessagesSince(LocalDateTime.now().minusDays(1));
        for (CameraMessage message : allCameraMessagesSince.get()) {
            speedFineService.checkForFine(message);
        }
        Optional<Vehicle> vehicle = vehicleService.getVehicleByProxyOrDb("4-ABC-123");
        assertTrue(vehicle.isPresent());
        List<Fine> fines = fineService.findAllByTypeAndVehicle(FineType.SPEED, vehicle.get());
        assertEquals(1, fines.size());
        assertEquals(FineType.SPEED, fines.get(0).getType());
    }

}