package be.kdg.processor.fine.evaluation;

import be.kdg.processor.camera.CameraService;
import be.kdg.processor.camera.message.CameraMessage;
import be.kdg.processor.camera.segment.Segment;
import be.kdg.processor.fine.Fine;
import be.kdg.processor.fine.FineService;
import be.kdg.processor.fine.FineType;
import be.kdg.processor.setting.SettingService;
import be.kdg.processor.shared.exception.ProcessorException;
import be.kdg.processor.shared.utils.DateUtil;
import be.kdg.processor.vehicle.Vehicle;
import be.kdg.processor.vehicle.VehicleService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

/**
 * Evaluates the speed of a vehicle.
 * If the speed of the vehicle is higher than the maximum speed on the segment, a fine is added to the vehicle.
 */
@Service
public class SpeedFineService extends FineEvaluationService {

    public SpeedFineService(VehicleService vehicleService, DateUtil dateUtil, FineService fineService, SettingService settingService, CameraService cameraService) {
        super(vehicleService, dateUtil, fineService, settingService, cameraService);
    }

    @Override
    public void checkForFine(CameraMessage cameraMessage) throws ProcessorException {
        Segment segment = cameraService.findSegmentByCameraMessage(cameraMessage);
        CameraMessage otherMessage = cameraService.getConnectedCameraMessage(segment, cameraMessage);
        checkSpeed(cameraMessage, otherMessage, segment);
    }

    private void checkSpeed(CameraMessage message1, CameraMessage message2, Segment segment) throws ProcessorException {
        double maxSpeed = segment.getSpeedLimit();
        double actualSpeed = calculateSpeed((double) segment.getDistance(), message1, message2);
        if (actualSpeed > maxSpeed) {
            Vehicle vehicle = getVehicle(message1);
            double price = calculatePrice(vehicle, actualSpeed, maxSpeed);
            List<CameraMessage> cameraMessages = Arrays.asList(message1, message2);
            createFine(new Fine(FineType.SPEED, price, actualSpeed, maxSpeed), cameraMessages);
        }
    }

    public double calculateSpeed(double distance, CameraMessage message1, CameraMessage message2) {
        LocalDateTime timestamp1 = message1.getTimestamp();
        LocalDateTime timestamp2 = message2.getTimestamp();
        double hours = dateUtil.getHoursBetweenDates(timestamp1, timestamp2);
        double km = distance / 1000;
        return km / hours;
    }

    private double calculatePrice(Vehicle vehicle, double curSpeed, double maxSpeed) {
        double price = (curSpeed - maxSpeed) * curSpeed;
        return super.calculateFineHistoryPrice(fineService.findAllByTypeAndVehicle(FineType.SPEED, vehicle), price);
    }
}
