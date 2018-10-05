package be.kdg.processor.fine;

import be.kdg.processor.shared.converters.IoConverter;
import be.kdg.processor.vehicle.LicensePlateDTO;
import be.kdg.processor.camera.couple.CameraCouple;
import be.kdg.processor.camera.message.CameraMessage;
import be.kdg.processor.vehicle.ProxyLicensePlateService;
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
    private final IoConverter ioConverter;

    @Override
    public void checkForFine(CameraCouple couple) {
        List<CameraMessage> msgs = new ArrayList<>();
        couple.getCameras().forEach(camera -> msgs.addAll(camera.getCameraMessages()));
        msgs.forEach(message -> {
            String json = proxyLicensePlateService.get(message.getLicensePlate());
            LicensePlateDTO licensePlateDTO = ioConverter.readJson(json, LicensePlateDTO.class);
            if (message.getCamera().getEuroNorm() > licensePlateDTO.getEuroNumber()) {
                LOGGER.info("camera " + message.getCamera().getId() + " detected vehicle below euronorm: " + licensePlateDTO.getPlateId());
            }
        });
    }
}
