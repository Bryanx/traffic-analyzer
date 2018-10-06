package be.kdg.processor.camera;

import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CameraServiceImplTest {
    public static final String msg1 = "<CameraMessage><cameraId>4</cameraId><licensePlate>2-ABC-123</licensePlate><timestamp>2018-10-05T10:56:39.4985467</timestamp><delay>100</delay></CameraMessage>";
    public static final String msg2 = "<CameraMessage><cameraId>5</cameraId><licensePlate>2-ABC-123</licensePlate><timestamp>2018-10-05T10:56:41.5003778</timestamp><delay>2000</delay></CameraMessage>";

}