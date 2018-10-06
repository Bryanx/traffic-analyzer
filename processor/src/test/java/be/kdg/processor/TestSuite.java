package be.kdg.processor;

import be.kdg.processor.camera.proxy.ProxyCameraServiceImplTest;
import be.kdg.processor.shared.converters.IoConverterTest;
import be.kdg.processor.shared.utils.DateUtilTest;
import be.kdg.processor.vehicle.ProxyLicensePlateServiceImplTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        ProxyCameraServiceImplTest.class,
        ProxyLicensePlateServiceImplTest.class,
        DateUtilTest.class,
        IoConverterTest.class
})
public class TestSuite {
}