package be.kdg.processor;

import be.kdg.processor.config.converters.IoConverterTest;
import be.kdg.processor.config.helpers.DateUtilTest;
import be.kdg.processor.config.mappers.CameraMessageMapperTest;
import be.kdg.processor.services.ThirdParty.ProxyCameraServiceImplTest;
import be.kdg.processor.services.ThirdParty.ProxyLicensePlateServiceImplTest;
import be.kdg.processor.services.impl.CameraServiceImplTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        ProxyCameraServiceImplTest.class,
        ProxyLicensePlateServiceImplTest.class,
        DateUtilTest.class,
        CameraMessageMapperTest.class,
        IoConverterTest.class
})
public class TestSuite {
}