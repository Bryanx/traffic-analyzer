package be.kdg.processor.fine.evaluation;

import be.kdg.processor.camera.Camera;
import be.kdg.processor.camera.CameraRepository;
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
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest
public class EmissionFineServiceTest {

    @Autowired
    private EmissionFineService emissionFineService;

    @Autowired
    private VehicleService vehicleService;

    @Autowired
    private FineService fineService;

    @Autowired
    private CameraRepository cameraRepository;

    @Test
    public void alreadyFined() {
        Vehicle vehicle = new Vehicle();
        vehicle.setPlateId("123-ABC-2");
        vehicle.setEuroNumber(2);
        CameraMessage msg1 = new CameraMessage("2-ABC-123", LocalDateTime.now().minusHours(1));
        CameraMessage msg2 = new CameraMessage("2-ABC-123", LocalDateTime.now());
        emissionFineService.createFine(new Fine(FineType.EMISSION, 150, 4, 2),
                vehicle, Arrays.asList(msg1, msg2));
        List<Fine> oldFines = fineService.findAllByTypeAndVehicle(FineType.EMISSION, vehicle);
        assertTrue(emissionFineService.alreadyFined(oldFines));
    }

    @Test
    public void checkForFine() {
        Camera camera = new Camera(3);
        camera.setEuroNorm(5);
        CameraMessage cameraMessage = new CameraMessage("2-ABC-123", LocalDateTime.now());
        cameraMessage.setCamera(camera);
        cameraRepository.saveAndFlush(camera);
        emissionFineService.checkForFine(cameraMessage);
        Optional<Vehicle> vehicle = vehicleService.getVehicleByProxyOrDb("2-ABC-123");
        assertTrue(vehicle.isPresent());
        List<Fine> fines = fineService.findAllByTypeAndVehicle(FineType.EMISSION, vehicle.get());
        assertTrue(fines.size() > 0);
    }
}