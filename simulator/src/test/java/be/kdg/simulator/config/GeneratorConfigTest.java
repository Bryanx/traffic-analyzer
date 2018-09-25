package be.kdg.simulator.config;

import be.kdg.simulator.messaging.generators.MessageGenerator;
import be.kdg.simulator.model.CameraMessage;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class GeneratorConfigTest {

    @Autowired
    private MessageGenerator messageGenerator;

    @Autowired
    private GeneratorConfig generatorConfig;

    @Test
    public void testMaxId() {
        CameraMessage cameraMessage = messageGenerator.generate();
        int maxId = generatorConfig.getMaxid();
        Assert.assertTrue(cameraMessage.getCameraId() <= maxId);
    }

}