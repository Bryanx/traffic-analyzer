package be.kdg.processor;

import be.kdg.processor.camera.proxy.ProxyCameraServiceImplTest;
import be.kdg.processor.fine.FineControllerTest;
import be.kdg.processor.fine.evaluation.EmissionFineServiceTest;
import be.kdg.processor.fine.evaluation.SpeedFineServiceTest;
import be.kdg.processor.shared.converters.IoConverterTest;
import be.kdg.processor.shared.utils.DateUtilTest;
import be.kdg.processor.vehicle.ProxyLicensePlateServiceImplTest;
import be.kdg.processor.vehicle.VehicleServiceImplTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        ProxyCameraServiceImplTest.class,
        ProxyLicensePlateServiceImplTest.class,
        DateUtilTest.class,
        IoConverterTest.class,
        FineControllerTest.class,
        VehicleServiceImplTest.class,
        SpeedFineServiceTest.class,
        EmissionFineServiceTest.class
})
public class TestSuite {
}