package be.kdg.processor.camera.proxy;

import be.kdg.processor.camera.Camera;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ProxyCameraServiceImplTest {

    //Add '--add-modules java.xml.bind' to VM options before running.
    @Autowired
    private ProxyCameraServiceImpl proxyCameraService;

    @Test
    public void get() {
        Camera result = proxyCameraService.get(1);
        System.out.println(result);
        Assert.assertNotNull(result);
    }
}