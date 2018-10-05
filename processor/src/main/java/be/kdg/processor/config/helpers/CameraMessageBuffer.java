package be.kdg.processor.config.helpers;

import be.kdg.processor.config.dtos.CameraMessageDTO;
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
public class CameraMessageBuffer extends HashSet<CameraMessageDTO> {
    private static final Logger LOGGER = LoggerFactory.getLogger(CameraMessageBuffer.class);

    /**
     * Removes a message from the buffer with the same licenseplate, but different cameraId, and returns it.
     */
    public CameraMessageDTO popMessageWithSamePlate(CameraMessageDTO inputMessage) {
        String plate = inputMessage.getLicensePlate();
        CameraMessageDTO message = super.stream()
                .filter(cameraMessage -> cameraMessage.getLicensePlate().equals(plate) &&
                        cameraMessage.getCameraId() != inputMessage.getCameraId())
                .findAny()
                .orElse(null);
        if (message != null) {
            LOGGER.info("Found 2 CameraMessages with the same licenseplate: " + plate);
            CameraMessageDTO temp = message;
            super.remove(temp);
            return temp;
        }
        return null;
    }

    @Override
    public void clear() {
        LOGGER.info("Removing {} messages from the buffer.", this.size());
        super.clear();
    }

    public List<CameraMessageDTO> copy() {
        return new ArrayList<>(this);
    }
}
