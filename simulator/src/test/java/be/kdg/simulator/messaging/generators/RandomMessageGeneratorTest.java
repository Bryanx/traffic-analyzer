package be.kdg.simulator.messaging.generators;

import be.kdg.simulator.model.CameraMessage;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.*;

public class RandomMessageGeneratorTest {

    private static final String LICENSE_PLATE_REGEX = "^[1-8]-[A-Z]{3}-[0-9]{2}[1-9]$";

    @Autowired
    private MessageGenerator messageGenerator;

    @Test
    public void testLicensePlate() {
        CameraMessage cameraMessage = messageGenerator.generate();
        Assert.assertTrue(cameraMessage.getLicensePlate().matches(LICENSE_PLATE_REGEX));
    }
}