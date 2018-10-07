package be.kdg.processor.fine;

import be.kdg.processor.camera.message.CameraMessage;
import be.kdg.processor.camera.segment.Segment;
import be.kdg.processor.vehicle.ProxyLicensePlateService;
import be.kdg.processor.vehicle.Vehicle;
import be.kdg.processor.vehicle.VehicleService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RequiredArgsConstructor
@Service
public class EmissionFineService implements FineService {
    private static final Logger LOGGER = LoggerFactory.getLogger(EmissionFineService.class);
    private final ProxyLicensePlateService proxyLicensePlateService;
    private final VehicleService vehicleService;

    private void createFine(int euroNorm, int actualNorm, double price, Vehicle vehicle, List<CameraMessage> msgs) {
        LOGGER.info(String.format("Camera %d detected vehicle below euronorm: %s (vehicle euronumber:%d, euronorm:%d).",
                msgs.get(0).getCameraId(),
                vehicle.getPlateId(),
                euroNorm,
                actualNorm));
        Fine fine = new Fine(FineType.EMISSION, price, euroNorm, actualNorm);
        vehicle.addFine(fine);
        fine.addCameraMessage(msgs.get(0));
        vehicleService.createVehicle(vehicle);
    }

    @Override
    public void checkForFine(Segment segment) {
        List<CameraMessage> msgs = new ArrayList<>();
        segment.getCameras().forEach(camera -> msgs.addAll(camera.getCameraMessages()));
        msgs.forEach(message -> {
            Vehicle vehicle = proxyLicensePlateService.get(message.getLicensePlate());
            int euroNorm = message.getCamera().getEuroNorm();
            int actualEuroNorm = vehicle.getEuroNumber();
            if (actualEuroNorm < euroNorm) {
                createFine(euroNorm, actualEuroNorm,0.0, vehicle, Arrays.asList(message));
            }
        });
    }
}
