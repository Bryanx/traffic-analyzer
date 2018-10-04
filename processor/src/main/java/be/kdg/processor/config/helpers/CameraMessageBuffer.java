package be.kdg.processor.config.helpers;

import be.kdg.processor.domain.CameraMessage;
import org.springframework.stereotype.Component;

import java.util.HashSet;

/**
 * Buffer that stores CameraMessages.
 * It gets cleared every x minutes, defined in the app.props
 * If 2 cameramessages with the same licenseplate are found, they are removed from the list.
 */
@Component
public class CameraMessageBuffer extends HashSet<CameraMessage> {

    public CameraMessage takeMessageWithSamePlate(String plate) {
        CameraMessage message = super.stream()
                .filter(cameraMessage -> cameraMessage.getLicensePlate().equals(plate))
                .findAny()
                .orElse(null);
        if (message != null) {
            CameraMessage temp = message;
            super.remove(temp);
            return temp;
        }
        return null;
    }
}
