package be.kdg.processor.vehicle;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ProxyLicensePlateServiceImplTest {

    //Add '--add-modules java.xml.bind' to VM options before running.
    @Autowired
    private ProxyLicensePlateServiceImpl proxyLicensePlateService;

    private static final String TEST_PLATE = "4-ABC-123";

    @Test
    public void get() {
        String result = proxyLicensePlateService.get(TEST_PLATE);
        System.out.println(result);
        assertTrue("Expected to get a result when calling LicensPlateProxyService, but got: " + result,
                result != null);
    }
}