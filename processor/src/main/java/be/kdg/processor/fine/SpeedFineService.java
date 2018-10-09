package be.kdg.processor.fine;

import be.kdg.processor.camera.message.CameraMessage;
import be.kdg.processor.camera.segment.Segment;
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
public class SpeedFineService implements FineService {
    private static final Logger LOGGER = LoggerFactory.getLogger(SpeedFineService.class);
    private final DateUtil dateUtil;
    private final VehicleService vehicleService;
    private final FineRepository fineRepository;

    @Override
    public void checkForFine(CameraMessage cameraMessage) {
        Segment segment = cameraMessage.getCamera().getSegment();
        if (segment == null) return;
        CameraMessage otherMessage = getConnectedCameraMessage(segment, cameraMessage);
        if (otherMessage == null) return;
        List<CameraMessage> cameraMessages = Arrays.asList(cameraMessage, otherMessage);

        double maxSpeed = segment.getSpeedLimit();
        double actualSpeed = calculateActualSpeed((double) segment.getDistance(), cameraMessages);
        if (actualSpeed > maxSpeed) {
            Vehicle vehicle = vehicleService.getVehicleByProxyOrDb(cameraMessage.getLicensePlate());
            double price = calculateFinePrice(actualSpeed, maxSpeed);
            createFine(new Fine(FineType.SPEED, price, actualSpeed, maxSpeed), vehicle, cameraMessages);
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
        Fine fineOut = fineRepository.saveAndFlush(fine);
        Vehicle vehicleOut = vehicleService.createVehicle(vehicle);
        fineOut.setVehicle(vehicleOut);
        fineOut.addCameraMessage(cameraMessages.get(0));
        fineOut.addCameraMessage(cameraMessages.get(1));
        fineRepository.save(fineOut);
    }

    private double calculateActualSpeed(double distance, List<CameraMessage> msgs) {
        LocalDateTime timestamp1 = msgs.get(0).getTimestamp();
        LocalDateTime timestamp2 = msgs.get(1).getTimestamp();
        double hours = dateUtil.getHoursBetweenDates(timestamp1, timestamp2);
        double km = distance / 1000;
        return km / hours;
    }

    private double calculateFinePrice(double curSpeed, double maxSpeed) {
        return (curSpeed - maxSpeed) * curSpeed;
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
