package be.kdg.simulator.messaging.messengers;

import be.kdg.simulator.model.CameraMessage;

public interface Messenger {
    void sendMessage(CameraMessage message);
}
