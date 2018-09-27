package be.kdg.simulator.messaging.generators;

import be.kdg.simulator.model.CameraMessage;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Repeat;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RandomMessageGeneratorTest {

    private static final String LICENSE_PLATE_REGEX = "^[1-8]-[A-Z]{3}-[0-9]{3}$";
    private static final int PLATE_TEST_AMOUNT = 100;

    @Autowired
    private MessageGenerator messageGenerator;

    @Repeat(value = PLATE_TEST_AMOUNT)
    @Test
    public void testLicensePlate() {
        String licensePlate = messageGenerator.generate().getLicensePlate();
        assertTrue(String.format("Expected string matching '%s'. Got: %s", LICENSE_PLATE_REGEX, licensePlate),
                licensePlate.matches(LICENSE_PLATE_REGEX));
    }
}