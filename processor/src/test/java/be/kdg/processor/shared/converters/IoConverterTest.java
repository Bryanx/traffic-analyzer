package be.kdg.processor.shared.converters;

import be.kdg.processor.camera.message.CameraMessageDTO;
import be.kdg.processor.camera.proxy.CameraProxyDTO;
import be.kdg.processor.vehicle.LicensePlateDTO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
public class IoConverterTest {
    public static final String JSON_PROXY_TEST = "{\"cameraId\":1,\"location\":{\"lat\":51.231932,\"long\":4.502442},\"segment\":{\"connectedCameraId\":2,\"distance\":4300,\"speedLimit\":70}}\n";
    public static final String JSON_PLATE_TEST = "{\"plateId\":\"4-ABC-123\",\"nationalNumber\":\"69.05.22-123.4\",\"euroNumber\":4}";
    public static final Integer expectedCameraId = 1;
    public static final String XML_TEST = "<CameraMessage><cameraId>2</cameraId><licensePlate>4-ABC-123</licensePlate><timestamp>2018-10-05T06:46:01.8174014</timestamp><delay>1000</delay></CameraMessage>";
    public static final String expectedLicensePlate = "4-ABC-123";

    @Autowired
    private IoConverter ioConverter;

    @Test
    public void readJson() {
        LicensePlateDTO resultDto = ioConverter.readJson(JSON_PLATE_TEST, LicensePlateDTO.class);
        System.out.println(resultDto);
        assertEquals("Expected licenseplate to be equal after json conversion.", resultDto.getPlateId(), expectedLicensePlate);

        CameraProxyDTO resultDto2 = ioConverter.readJson(JSON_PROXY_TEST, CameraProxyDTO.class);
        System.out.println(resultDto2);
        assertEquals("Expected cameraId to be equal after json conversion.", resultDto2.getCameraId(), expectedCameraId);
    }

    @Test
    public void readXml() {
        CameraMessageDTO resultDto = ioConverter.readXml(XML_TEST, CameraMessageDTO.class);
        System.out.println(resultDto);
        assertEquals("Expected licenseplate to be equal after xml conversion.", resultDto.getLicensePlate(), expectedLicensePlate);
    }
}