package be.kdg.processor.fine.evaluation;

import be.kdg.processor.camera.message.CameraMessage;
import be.kdg.processor.fine.Fine;
import be.kdg.processor.fine.FineService;
import be.kdg.processor.fine.FineType;
import be.kdg.processor.setting.Setting;
import be.kdg.processor.setting.SettingNotFoundException;
import be.kdg.processor.setting.SettingService;
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
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class EmissionFineService implements FineEvaluationService {
    private static final Logger LOGGER = LoggerFactory.getLogger(EmissionFineService.class);
    private static final String EMISSION_FACTOR_KEY = "emission-fine-factor";
    private static final double DEFAULT_EMISSION_FACTOR = 100.0;
    private final VehicleService vehicleService;
    private final DateUtil dateUtil;
    private final GeneralConfig generalConfig;
    private final FineService fineService;
    private final SettingService settingService;

    @Override
    public void checkForFine(CameraMessage cameraMessage) {
        Optional<Vehicle> vehicle = vehicleService.getVehicleByProxyOrDb(cameraMessage.getLicensePlate());
        if (!vehicle.isPresent() || alreadyFined(vehicle.get())) return;

        int euroNorm = cameraMessage.getCamera().getEuroNorm();
        int actualEuroNorm = vehicle.get().getEuroNumber();
        if (actualEuroNorm < euroNorm) {
            createFine(new Fine(FineType.EMISSION, getPrice(), euroNorm, actualEuroNorm), vehicle.get(), Arrays.asList(cameraMessage));
        }
    }

    private double getPrice() {
        try {
            Setting setting = settingService.findByKey(EMISSION_FACTOR_KEY);
            return setting.getValue();
        } catch (SettingNotFoundException e) {
            LOGGER.error(e.getMessage(),e);
        }
        return DEFAULT_EMISSION_FACTOR;
    }

    @Override
    public void createFine(Fine fine, Vehicle vehicle, List<CameraMessage> cameraMessages) {
        LOGGER.info(String.format("Creating emissionfine for: Camera %d detected vehicle below euronorm: %s (vehicle euronumber:%d, euronorm:%d).",
                cameraMessages.get(0).getCameraId(),
                vehicle.getPlateId(),
                fine.getActualNorm(),
                fine.getEuroNorm()));
        Fine fineOut = fineService.save(fine);
        Vehicle vehicleOut = vehicleService.createVehicle(vehicle);
        fineOut.setVehicle(vehicleOut);
        fineOut.addCameraMessage(cameraMessages.get(0));
        fineService.save(fineOut);
    }

    public boolean alreadyFined(Vehicle vehicle) {
        List<Fine> fines = fineService.findAllByVehicleIn(vehicle);
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
