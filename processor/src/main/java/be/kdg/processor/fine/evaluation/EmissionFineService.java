package be.kdg.processor.fine.evaluation;

import be.kdg.processor.camera.CameraService;
import be.kdg.processor.camera.message.CameraMessage;
import be.kdg.processor.fine.Fine;
import be.kdg.processor.fine.FineService;
import be.kdg.processor.fine.FineType;
import be.kdg.processor.setting.SettingService;
import be.kdg.processor.shared.exception.ProcessorException;
import be.kdg.processor.shared.utils.DateUtil;
import be.kdg.processor.vehicle.Vehicle;
import be.kdg.processor.vehicle.VehicleService;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

/**
 * Evaluates the vehicle in a CameraMessage.
 * If the eurnorm of the vehicle is below the euronorm in the camera, a fine is added to the vehicle.
 */
@Service
public class EmissionFineService extends FineEvaluationService {
    private static final String EMISSION_FACTOR_KEY = "emission-fine-factor";
    private static final double DEFAULT_EMISSION_FACTOR = 100.0;

    public EmissionFineService(VehicleService vehicleService, DateUtil dateUtil, FineService fineService, SettingService settingService, CameraService cameraService) {
        super(vehicleService, dateUtil, fineService, settingService, cameraService);
    }

    @Override
    public void checkForFine(CameraMessage cameraMessage) throws ProcessorException {
        Vehicle vehicle = getVehicle(cameraMessage);
        List<Fine> oldFines = fineService.findAllByTypeAndVehicle(FineType.EMISSION, vehicle);
        if (fineService.checkIfAlreadyHasEmissionFine(vehicle)) return;
        checkEuroNorm(cameraMessage, vehicle.getEuroNumber(), oldFines);
    }

    private void checkEuroNorm(CameraMessage cameraMessage, int actualEuroNorm, List<Fine> oldFines) throws ProcessorException {
        int euroNorm = cameraMessage.getCamera().getEuroNorm();
        if (actualEuroNorm < euroNorm) {
            createFine(new Fine(FineType.EMISSION, calculatePrice(oldFines), euroNorm, actualEuroNorm), Arrays.asList(cameraMessage));
        }
    }

    private double calculatePrice(List<Fine> oldFines) {
        double priceFromService = getSetting(EMISSION_FACTOR_KEY, DEFAULT_EMISSION_FACTOR);
        return super.calculateFineHistoryPrice(oldFines, priceFromService);
    }
}
