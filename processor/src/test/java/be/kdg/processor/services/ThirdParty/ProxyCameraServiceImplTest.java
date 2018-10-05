package be.kdg.processor.services.ThirdParty;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ProxyCameraServiceImplTest {

    @Autowired
    private ProxyCameraServiceImpl proxyCameraService;

    @Test
    public void get() {
        String result = proxyCameraService.get(1);
        assertTrue("Expected to get a result when calling CameraServiceProxy, but got: " + result, result != null);
    }
}