package be.kdg.processor.camera.message;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

/**
 * Buffer that stores CameraMessages.
 * It gets cleared every x minutes, defined in the app.props
 * If 2 cameramessages with the same licenseplate are found, they are removed from the list.
 */
@Component
public class CameraMessageBuffer extends HashSet<CameraMessage> {
    private static final Logger LOGGER = LoggerFactory.getLogger(CameraMessageBuffer.class);

    /**
     * Removes a message from the buffer with the same licenseplate, but different cameraId, and returns it.
     */
    public CameraMessage getMessageWithSamePlate(CameraMessage inputMessage) {
        String plate = inputMessage.getLicensePlate();
        Optional<CameraMessage> message = super.stream()
                .filter(cameraMessage -> cameraMessage.getLicensePlate().equals(plate) &&
                        cameraMessage.getCameraId() != inputMessage.getCameraId())
                .findAny();
        if (message.isPresent()) {
            LOGGER.info("Found 2 CameraMessages with the same licenseplate: " + plate);
            return message.get();
        }
        return null;
    }

    @Override
    public void clear() {
        LOGGER.info("Removing {} messages from the buffer.", this.size());
        super.clear();
    }

    public List<CameraMessage> copy() {
        return new ArrayList<>(this);
    }
}
