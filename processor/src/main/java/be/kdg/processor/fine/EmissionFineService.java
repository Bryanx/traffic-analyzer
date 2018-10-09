package be.kdg.processor.fine;

import be.kdg.processor.camera.message.CameraMessage;
import be.kdg.processor.shared.GeneralConfig;
import be.kdg.processor.shared.utils.DateUtil;
import be.kdg.processor.vehicle.Vehicle;
import be.kdg.processor.vehicle.VehicleService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@RequiredArgsConstructor
@Service
public class EmissionFineService implements FineService {
    private static final Logger LOGGER = LoggerFactory.getLogger(EmissionFineService.class);
    private final VehicleService vehicleService;
    private final DateUtil dateUtil;
    private final GeneralConfig generalConfig;
    private final FineRepository fineRepository;

    @Override
    public void checkForFine(CameraMessage cameraMessage) {
        vehicleService.getVehicleByProxyOrDb(cameraMessage.getLicensePlate()).ifPresent(vehicle -> {
            if (alreadyFined(vehicle)) return;
            int euroNorm = cameraMessage.getCamera().getEuroNorm();
            int actualEuroNorm = vehicle.getEuroNumber();
            if (actualEuroNorm < euroNorm) {
                createFine(new Fine(FineType.EMISSION, 0.0, euroNorm, actualEuroNorm), vehicle, Arrays.asList(cameraMessage));
            }
        });
    }

    @Override
    public void createFine(Fine fine, Vehicle vehicle, List<CameraMessage> cameraMessages) {
        LOGGER.info(String.format("Creating emissionfine for: Camera %d detected vehicle below euronorm: %s (vehicle euronumber:%d, euronorm:%d).",
                cameraMessages.get(0).getCameraId(),
                vehicle.getPlateId(),
                fine.getActualNorm(),
                fine.getEuroNorm()));
        Fine fineOut = fineRepository.saveAndFlush(fine);
        Vehicle vehicleOut = vehicleService.createVehicle(vehicle);
        fineOut.setVehicle(vehicleOut);
        fineOut.addCameraMessage(cameraMessages.get(0));
        fineRepository.saveAndFlush(fineOut);
    }

    private boolean alreadyFined(Vehicle vehicle) {
        List<Fine> fines = fineRepository.findAllByVehicleIn(vehicle);
        for (Fine fine : fines) {
            if (fine.getType() == FineType.EMISSION) {
                double hoursSinceFine = dateUtil.getHoursBetweenDates(LocalDateTime.now(), fine.getCreationDate());
                if (hoursSinceFine < generalConfig.getEmissionFineTimeBetween()) {
                    LOGGER.debug("Found vehicle that was already fined: " + vehicle.getPlateId());
                    return true;
                }
            }
        }
        return false;
    }
}
