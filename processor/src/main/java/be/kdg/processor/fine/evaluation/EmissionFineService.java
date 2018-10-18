package be.kdg.processor.fine.evaluation;

import be.kdg.processor.camera.message.CameraMessage;
import be.kdg.processor.fine.Fine;
import be.kdg.processor.fine.FineService;
import be.kdg.processor.fine.FineType;
import be.kdg.processor.setting.SettingService;
import be.kdg.processor.shared.utils.DateUtil;
import be.kdg.processor.vehicle.Vehicle;
import be.kdg.processor.vehicle.VehicleService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Service
public class EmissionFineService extends FineEvaluationService {
    private static final String EMISSION_FACTOR_KEY = "emission-fine-factor";
    private static final String EMISSION_DELAY_KEY = "emission-fine-delay";
    private static final double DEFAULT_EMISSION_FACTOR = 100.0;
    private static final double DEFAULT_EMISSION_DELAY = 24;

    public EmissionFineService(VehicleService vehicleService, DateUtil dateUtil, FineService fineService, SettingService settingService) {
        super(vehicleService, dateUtil, fineService, settingService);
    }

    @Override
    public void checkForFine(CameraMessage cameraMessage) {
        Vehicle vehicle = getVehicle(cameraMessage);
        if (vehicle == null) return;
        List<Fine> oldFines = fineService.findAllByTypeAndVehicle(FineType.EMISSION, vehicle);
        if (alreadyFined(oldFines)) return;
        checkEuroNorm(cameraMessage, vehicle, oldFines);
    }

    private void checkEuroNorm(CameraMessage cameraMessage, Vehicle vehicle, List<Fine> oldFines) {
        int euroNorm = cameraMessage.getCamera().getEuroNorm();
        int actualEuroNorm = vehicle.getEuroNumber();
        if (actualEuroNorm < euroNorm) {
            createFine(new Fine(FineType.EMISSION, calculatePrice(oldFines), euroNorm, actualEuroNorm), vehicle, Arrays.asList(cameraMessage));
        }
    }

    private double calculatePrice(List<Fine> oldFines) {
        double priceFromService = getSetting(EMISSION_FACTOR_KEY, DEFAULT_EMISSION_FACTOR);
        return super.calculateFineHistoryPrice(oldFines, priceFromService);
    }

    public boolean alreadyFined(List<Fine> oldFines) {
        double emissionDelay = getSetting(EMISSION_DELAY_KEY, DEFAULT_EMISSION_DELAY);
        return oldFines.stream()
                .mapToDouble(fine -> dateUtil.getHoursBetweenDates(LocalDateTime.now(), fine.getCreationDate()))
                .anyMatch(hoursSinceFine -> hoursSinceFine < emissionDelay);
    }
}
