package be.kdg.processor.vehicle;

import be.kdg.processor.fine.Fine;
import be.kdg.processor.fine.FineRepository;
import be.kdg.processor.fine.FineType;
import be.kdg.processor.shared.exception.ProcessorException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest
public class VehicleServiceImplTest {

    @Autowired
    private VehicleServiceImpl vehicleService;

    @Autowired
    private FineRepository fineRepository;

    @Test
    public void createVehicle() throws ProcessorException {
        Fine fine = new Fine(FineType.EMISSION, 0.0, 1, 1);
        Vehicle vehicle = new Vehicle();
        fineRepository.saveAndFlush(fine);
        vehicle.addFine(fine);
        vehicle.setPlateId("123");
        vehicle.setEuroNumber(1);
        vehicle.setNationalNumber("123");
        vehicleService.createVehicle(vehicle);
        Vehicle foundVehicle = vehicleService.findByLicensePlate("123");
        assertNotNull(foundVehicle.getFines());
    }
}