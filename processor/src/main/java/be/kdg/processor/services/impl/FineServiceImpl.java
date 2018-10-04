package be.kdg.processor.services.impl;

import be.kdg.processor.domain.CameraCouple;
import be.kdg.processor.domain.CameraMessage;
import be.kdg.processor.domain.Fine;
import be.kdg.processor.domain.FineType;
import be.kdg.processor.persistence.api.FineRepository;
import be.kdg.processor.services.api.FineService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service("fineService")
public class FineServiceImpl implements FineService {
    private static final Logger LOGGER = LoggerFactory.getLogger(FineServiceImpl.class);
    private final FineRepository fineRepository;
    private final List<FineType> fineTypes = new ArrayList<>();

    @Override
    public Fine createFine(Fine fine) {
        return fineRepository.save(fine);
    }

    public void createSpeedFine(double curSpeed, double maxSpeed, CameraMessage msg1, CameraMessage msg2) {
        double price = (curSpeed - maxSpeed) * curSpeed;
        List<CameraMessage> msgs = new ArrayList<>();
        msgs.add(msg1);
        msgs.add(msg2);
         createFine(new Fine(FineType.SPEED, price, msgs));
    }

    public void calculateFines(CameraCouple couple, CameraMessage msg1, CameraMessage msg2) {
//        fineTypes.forEach(fineType -> {
//            if (fineType == FineType.SPEED) {
//                int maxSpeed = couple.getMaxSpeed();
//                //TODO: make method for this to get hour amount
//                double timeTaken = msg2.getTimestamp() - msg1.getTimestamp();
//                double curSpeed = couple.getDistance() / timeTaken;
//                if (curSpeed > maxSpeed) {
//                    createSpeedFine(curSpeed,maxSpeed,msg1,msg2);
//                }
//            }
//            if (fineType == FineType.EMISSION) {
//                List<Camera> cameras = couple.getCameras();
//                if (cameras.stream().filter(c -> c.getEuroNorm() != null) != null) {
//                    //get licenseplate
//                    //json = proxy.get(plate)
//                    //json contains euronorm
//                    //if euronorm > cameraEuronorm
//                }
//            }
//        });
    }
}
