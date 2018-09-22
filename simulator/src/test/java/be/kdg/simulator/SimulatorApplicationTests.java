package be.kdg.simulator;

import be.kdg.simulator.messaging.generators.MessageGenerator;
import be.kdg.simulator.model.CameraMessage;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.junit.Assert;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SimulatorApplicationTests {

	@Autowired
	private MessageGenerator messageGenerator;

	@Test
	public void testMessageGenerator() {
		CameraMessage cameraMessage = messageGenerator.generate();
		Assert.assertTrue(cameraMessage.getLicensePlate().matches("^[1-8]-[A-Z]{3}-[0-9]{2}[1-9]$"));
	}

}
