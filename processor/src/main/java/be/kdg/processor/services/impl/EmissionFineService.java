package be.kdg.processor.services.impl;

import be.kdg.processor.config.converters.IoConverter;
import be.kdg.processor.config.dtos.LicensePlateDTO;
import be.kdg.processor.domain.CameraCouple;
import be.kdg.processor.domain.CameraMessage;
import be.kdg.processor.services.ThirdParty.ProxyLicensePlateService;
import be.kdg.processor.services.api.FineService;
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
            if ( message.getCamera().getEuroNorm() < licensePlateDTO.getEuroNumber()) {
                LOGGER.info("Camera detected vehicle below euronorm: " + licensePlateDTO.getPlateId());
            }
        });
    }
}
