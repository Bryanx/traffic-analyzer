package be.kdg.processor.camera.proxy;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ProxyCameraServiceImplTest {

    //Add '--add-modules java.xml.bind' to VM options before running.
    @Autowired
    private ProxyCameraServiceImpl proxyCameraService;

    @Test
    public void get() {
        String result = proxyCameraService.get(3);
        System.out.println(result);
        assertTrue("Expected to get a result when calling CameraServiceProxy, but got: " + result, result != null);
    }
}