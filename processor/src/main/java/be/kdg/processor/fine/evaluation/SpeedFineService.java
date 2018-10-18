package be.kdg.processor.fine.evaluation;

import be.kdg.processor.camera.message.CameraMessage;
import be.kdg.processor.camera.segment.Segment;
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
public class SpeedFineService extends FineEvaluationService {

    public SpeedFineService(VehicleService vehicleService, DateUtil dateUtil, FineService fineService, SettingService settingService) {
        super(vehicleService, dateUtil, fineService, settingService);
    }

    @Override
    public void checkForFine(CameraMessage cameraMessage) {
        Segment segment = cameraMessage.getCamera().getSegment();
        if (segment == null) return;
        CameraMessage otherMessage = getConnectedCameraMessage(segment, cameraMessage);
        if (otherMessage == null) return;

        double maxSpeed = segment.getSpeedLimit();
        double actualSpeed = calculateSpeed((double) segment.getDistance(), cameraMessage, otherMessage);
        if (actualSpeed > maxSpeed) {
            Vehicle vehicle = getVehicle(cameraMessage);
            if (vehicle == null) return;
            double price = calculatePrice(vehicle, actualSpeed, maxSpeed);
            List<CameraMessage> cameraMessages = Arrays.asList(cameraMessage, otherMessage);
            createFine(new Fine(FineType.SPEED, price, actualSpeed, maxSpeed), vehicle, cameraMessages);
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

    private CameraMessage getConnectedCameraMessage(Segment segment, CameraMessage cameraMessage) {
        return segment.getCameras().stream()
                .filter(camera -> camera.getCameraId() == segment.getConnectedCameraId())
                .flatMap(c -> c.getCameraMessages().stream())
                .filter(message -> cameraMessage != message &&
                        cameraMessage.getLicensePlate().equals(message.getLicensePlate()))
                .findFirst()
                .orElse(null);
    }
}
