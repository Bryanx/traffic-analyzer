package be.kdg.processor.vehicle;

import be.kdg.processor.shared.exception.ProcessorException;
import be.kdg.processor.vehicle.proxy.ProxyLicensePlateServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ProxyLicensePlateServiceImplTest {

    //Add '--add-modules java.xml.bind' to VM options before running.
    @Autowired
    private ProxyLicensePlateServiceImpl proxyLicensePlateService;

    private static final String TEST_PLATE = "4-ABC-123";

    @Test
    public void get() throws ProcessorException {
        Vehicle vehicle = proxyLicensePlateService.fetchVehicle(TEST_PLATE);
        assertEquals(vehicle.getPlateId(), TEST_PLATE);
    }
}