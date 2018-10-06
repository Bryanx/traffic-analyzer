package be.kdg.processor.fine;

import be.kdg.processor.camera.couple.CameraCouple;
import be.kdg.processor.camera.message.CameraMessage;
import be.kdg.processor.shared.converters.IoConverter;
import be.kdg.processor.vehicle.ProxyLicensePlateService;
import be.kdg.processor.vehicle.Vehicle;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class EmissionFineService implements FineService {
    private static final Logger LOGGER = LoggerFactory.getLogger(EmissionFineService.class);
    private final ProxyLicensePlateService proxyLicensePlateService;

    @Override
    public void checkForFine(CameraCouple couple) {
        List<CameraMessage> msgs = new ArrayList<>();
        couple.getCameras().forEach(camera -> msgs.addAll(camera.getCameraMessages()));
        msgs.forEach(message -> {
            Vehicle vehicle = proxyLicensePlateService.get(message.getLicensePlate());
            if (message.getCamera().getEuroNorm() > vehicle.getEuroNumber()) {
                LOGGER.info("camera " + message.getCamera().getCameraId() + " detected vehicle below euronorm: " + vehicle.getPlateId());
            }
        });
    }
}
