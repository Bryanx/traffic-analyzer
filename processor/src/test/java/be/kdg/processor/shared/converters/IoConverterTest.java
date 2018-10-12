package be.kdg.processor.shared.converters;

import be.kdg.processor.camera.Camera;
import be.kdg.processor.vehicle.Vehicle;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest
public class IoConverterTest {
    private static final String JSON_PROXY_TEST = "{\"cameraId\":1,\"location\":{\"lat\":51.231932,\"long\":4.502442},\"segment\":{\"connectedCameraId\":2,\"distance\":4300,\"speedLimit\":70}}\n";
    private static final String JSON_PLATE_TEST = "{\"plateId\":\"4-ABC-123\",\"nationalNumber\":\"69.05.22-123.4\",\"euroNumber\":4}";
    private static final int expectedCameraId = 1;
    private static final String expectedLicensePlate = "4-ABC-123";

    @Autowired
    private IoConverter ioConverter;

    @Test
    public void readJson() {
        Optional<Vehicle> vehicle = ioConverter.readJson(JSON_PLATE_TEST, Vehicle.class);
        assertTrue(vehicle.isPresent());
        assertEquals(vehicle.get().getPlateId(), expectedLicensePlate);

        Optional<Camera> camera = ioConverter.readJson(JSON_PROXY_TEST, Camera.class);
        System.out.println(camera);
        assertTrue(camera.isPresent());
        assertEquals(camera.get().getCameraId(), expectedCameraId);
    }
}