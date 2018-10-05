package be.kdg.processor.config.converters;

import be.kdg.processor.config.dtos.CameraMessageDTO;
import be.kdg.processor.config.dtos.LicensePlateDTO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
public class IoConverterTest {

    public static final String JSON_TEST = "{\"plateId\":\"4-ABC-123\",\"nationalNumber\":\"69.05.22-123.4\",\"euroNumber\":4}";
    public static final String XML_TEST = "<CameraMessage><cameraId>2</cameraId><licensePlate>4-ABC-123</licensePlate><timestamp>2018-10-05T06:46:01.8174014</timestamp><delay>1000</delay></CameraMessage>";
    public static final String expectedLicensePlate = "4-ABC-123";

    @Autowired
    private IoConverter ioConverter;

    @Test
    public void jsonToObject() {
        LicensePlateDTO resultDto = ioConverter.jsonToObject(JSON_TEST);
        System.out.println(resultDto);
        assertEquals("Expected licenseplate to be equal after json conversion.", resultDto.getPlateId(), expectedLicensePlate);
    }

    @Test
    public void testXML() {
        CameraMessageDTO resultDto = ioConverter.readXml(XML_TEST);
        System.out.println(resultDto);
        assertEquals("Expected licenseplate to be equal after xml conversion.", resultDto.getLicensePlate(), expectedLicensePlate);
    }
}