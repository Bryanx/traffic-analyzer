package be.kdg.processor.config.helpers;

import be.kdg.processor.domain.CameraMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * Buffer that stores CameraMessages.
 * It gets cleared every x minutes, defined in the app.props
 * If 2 cameramessages with the same licenseplate are found, they are removed from the list.
 */
@Component
public class CameraMessageBuffer extends HashSet<CameraMessage> {
    private static final Logger LOGGER = LoggerFactory.getLogger(CameraMessageBuffer.class);

    public CameraMessage popMessageWithSamePlate(CameraMessage inputMessage) {
        String plate = inputMessage.getLicensePlate();
        CameraMessage message = super.stream()
                .filter(cameraMessage -> cameraMessage.getLicensePlate().equals(plate))
                .findAny()
                .orElse(null);
        if (message != null) {
            LOGGER.info("Found 2 CameraMessages with the same licenseplate: " + plate + ". Removing them from buffer.");
            CameraMessage temp = message;
            super.remove(temp);
            return temp;
        }
        return null;
    }

    @Override
    public boolean add(CameraMessage cameraMessage) {
        boolean succeeded = super.add(cameraMessage);
        if (succeeded) {
            LOGGER.info("Stored message in buffer: {}", cameraMessage);
        } else {
            LOGGER.error("Failed to add message to buffer: {}", cameraMessage);
        }
        return succeeded;
    }

    public List<CameraMessage> empty() {
        List<CameraMessage> messages = new ArrayList<>(this);
        super.clear();
        return messages;
    }
}
