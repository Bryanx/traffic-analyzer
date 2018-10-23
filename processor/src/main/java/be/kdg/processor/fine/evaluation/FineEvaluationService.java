package be.kdg.processor.fine.evaluation;

import be.kdg.processor.camera.message.CameraMessage;
import be.kdg.processor.camera.segment.Segment;
import be.kdg.processor.fine.Fine;
import be.kdg.processor.fine.FineService;
import be.kdg.processor.setting.web.SettingNotFoundException;
import be.kdg.processor.setting.SettingService;
import be.kdg.processor.shared.utils.DateUtil;
import be.kdg.processor.vehicle.Vehicle;
import be.kdg.processor.vehicle.VehicleService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Abstract evaluation service class.
 * This class can be called in a list to contain all the derived fine evaluations.
 */
@RequiredArgsConstructor
public abstract class FineEvaluationService {
    static final Logger LOGGER = LoggerFactory.getLogger(FineEvaluationService.class);
    final VehicleService vehicleService;
    final DateUtil dateUtil;
    final FineService fineService;
    final SettingService settingService;

    public abstract void checkForFine(CameraMessage cameraMessage);

    void createFine(Fine fine, List<CameraMessage> cameraMessages) {
        Vehicle vehicle = getVehicle(cameraMessages.get(0));
        LOGGER.info(String.format("Creating %s fine for vehicle %s. Detected by camera('s) %s",
                fine.getType(),
                vehicle.getPlateId(),
                cameraMessages.stream().map(CameraMessage::getCameraId).collect(Collectors.toList())));
        Fine fineOut = fineService.save(fine);
        Vehicle vehicleOut = vehicleService.createVehicle(vehicle);
        fineOut.setVehicle(vehicleOut);
        cameraMessages.forEach(fineOut::addCameraMessage);
        fineService.save(fineOut);
    }

    Vehicle getVehicle(CameraMessage cameraMessage) {
        return vehicleService.getVehicleByProxyOrDb(cameraMessage.getLicensePlate()).orElse(null);
    }

    /**
     * Retrieves a setting from the database.
     *
     * @param key          The key for the requested setting.
     * @param defaultValue If the setting is not found this value is returned.
     * @return Value of the setting.
     */
    double getSetting(String key, double defaultValue) {
        try {
            return settingService.findByKey(key).getValue();
        } catch (SettingNotFoundException e) {
            LOGGER.error(e.getMessage(), e);
        }
        return defaultValue;
    }

    /**
     * The fine price is increased with 20 for each previous speedfine.
     *
     * @param oldFines         previous fines
     * @param priceFromService original price.
     * @return new price.
     */
    double calculateFineHistoryPrice(List<Fine> oldFines, double priceFromService) {
        return priceFromService + oldFines.size() * 20;
    }

    /**
     * For a given segment and cameramessage, finds the corresponding cameramessage.
     */
    CameraMessage getConnectedCameraMessage(Segment segment, CameraMessage cameraMessage) {
        return segment.getCameras().stream()
                .filter(camera -> camera.getCameraId() == segment.getConnectedCameraId())
                .flatMap(c -> c.getCameraMessages().stream())
                .filter(message -> cameraMessage != message &&
                        cameraMessage.getLicensePlate().equals(message.getLicensePlate()))
                .findFirst()
                .orElse(null);
    }
}
