package be.kdg.processor.fine;

import be.kdg.processor.camera.couple.CameraCouple;
import be.kdg.processor.camera.message.CameraMessage;
import be.kdg.processor.shared.utils.DateUtil;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class SpeedFineService implements FineService {
    private static final Logger LOGGER = LoggerFactory.getLogger(SpeedFineService.class);
    private final DateUtil dateUtil;
    private final FineRepository fineRepository;

    private Fine createFine(double actualSpeed, double maxSpeed, double price, List<CameraMessage> msgs) {
        LOGGER.info(String.format("Creating fine for %s. Vehicle was driving: %.1f km/h, where only %.1f km/h is allowed",
                msgs.get(0).getLicensePlate(), actualSpeed, maxSpeed));
        return fineRepository.save(
                new Fine(FineType.SPEED, actualSpeed, maxSpeed, price, msgs)
        );
    }

    public void checkForFine(CameraCouple couple) {
        List<CameraMessage> msgs = new ArrayList<>();
        couple.getCameras().forEach(camera -> msgs.addAll(camera.getCameraMessages()));
        double maxSpeed = couple.getMaxSpeed();
        double actualSpeed = calculateActualSpeed((double) couple.getDistance(), msgs);
        if (actualSpeed > maxSpeed) {
            double price = calculateFinePrice(actualSpeed, maxSpeed);
            createFine(actualSpeed, maxSpeed, price, msgs);
        }
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
}
