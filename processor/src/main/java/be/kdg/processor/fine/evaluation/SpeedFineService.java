package be.kdg.processor.fine.evaluation;

import be.kdg.processor.camera.message.CameraMessage;
import be.kdg.processor.camera.segment.Segment;
import be.kdg.processor.fine.Fine;
import be.kdg.processor.fine.FineService;
import be.kdg.processor.fine.FineType;
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
public class SpeedFineService implements FineEvaluationService {
    private static final Logger LOGGER = LoggerFactory.getLogger(SpeedFineService.class);
    private final DateUtil dateUtil;
    private final VehicleService vehicleService;
    private final FineService fineService;

    @Override
    public void checkForFine(CameraMessage cameraMessage) {
        Segment segment = cameraMessage.getCamera().getSegment();
        if (segment == null) return;
        Optional<CameraMessage> otherMessage = getConnectedCameraMessage(segment, cameraMessage);
        if (!otherMessage.isPresent()) return;


        double maxSpeed = segment.getSpeedLimit();
        double actualSpeed = calculateSpeed((double) segment.getDistance(), cameraMessage, otherMessage.get());
        if (actualSpeed > maxSpeed) {
            vehicleService.getVehicleByProxyOrDb(cameraMessage.getLicensePlate()).ifPresent(vehicle -> {
                double price = calculateFinePrice(actualSpeed, maxSpeed);
                List<CameraMessage> cameraMessages = Arrays.asList(cameraMessage, otherMessage.get());
                createFine(new Fine(FineType.SPEED, price, actualSpeed, maxSpeed), vehicle, cameraMessages);
            });
        }
    }

    @Override
    public void createFine(Fine fine, Vehicle vehicle, List<CameraMessage> cameraMessages) {
        LOGGER.info(String.format("Creating speedfine for %s. (Cams %d-%d) Vehicle was driving: %.1f km/h, where only %.1f km/h is allowed",
                cameraMessages.get(0).getLicensePlate(),
                cameraMessages.get(0).getCameraId(),
                cameraMessages.get(1).getCameraId(),
                fine.getActualSpeed(),
                fine.getMaxSpeed()));
        Fine fineOut = fineService.save(fine);
        Vehicle vehicleOut = vehicleService.createVehicle(vehicle);
        fineOut.setVehicle(vehicleOut);
        fineOut.addCameraMessage(cameraMessages.get(0));
        fineOut.addCameraMessage(cameraMessages.get(1));
        fineService.save(fineOut);
    }

    private double calculateSpeed(double distance, CameraMessage message1, CameraMessage message2) {
        LocalDateTime timestamp1 = message1.getTimestamp();
        LocalDateTime timestamp2 = message2.getTimestamp();
        double hours = dateUtil.getHoursBetweenDates(timestamp1, timestamp2);
        double km = distance / 1000;
        return km / hours;
    }

    private double calculateFinePrice(double curSpeed, double maxSpeed) {
        return (curSpeed - maxSpeed) * curSpeed;
    }

    private Optional<CameraMessage> getConnectedCameraMessage(Segment segment, CameraMessage cameraMessage) {
        return segment.getCameras().stream()
                .filter(camera -> camera.getCameraId() == segment.getConnectedCameraId())
                .flatMap(c -> c.getCameraMessages().stream())
                .filter(message -> cameraMessage != message &&
                        cameraMessage.getLicensePlate().equals(message.getLicensePlate()))
                .findFirst();
    }
}
