package be.kdg.simulator.config;

import be.kdg.simulator.messaging.generators.MessageGenerator;
import be.kdg.simulator.model.CameraMessage;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Repeat;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class GeneratorConfigTest {

    private static final int MAXID_TEST_AMOUNT = 100;

    @Autowired
    private MessageGenerator messageGenerator;

    @Autowired
    private GeneratorConfig generatorConfig;

    @Repeat(value = MAXID_TEST_AMOUNT)
    @Test
    public void testMaxId() {
        int cameraId = messageGenerator.generate().getCameraId();
        int maxId = generatorConfig.getMaxid();
        Assert.assertTrue(String.format("Expected camera id to be smaller than '%s'. Got: %s", maxId, cameraId),
                cameraId <= maxId);
    }

}