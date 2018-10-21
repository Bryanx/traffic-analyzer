package be.kdg.processor.camera.message.receiver;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.listener.RabbitListenerEndpointRegistry;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

/**
 * Rest controller to start and stop receiving messages from the queue.
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class ReceiverController {
    private static final Logger LOGGER = LoggerFactory.getLogger(ReceiverController.class);
    private final RabbitListenerEndpointRegistry rabbitListenerEndpointRegistry;

    @PostMapping("/receiver/start")
    @ResponseStatus(value = HttpStatus.OK)
    public void start() {
        LOGGER.info("Started receiving mesasges from queue.");
        rabbitListenerEndpointRegistry.getListenerContainer("cameraMessageReceiver").start();
    }

    @PostMapping("/receiver/stop")
    @ResponseStatus(value = HttpStatus.OK)
    public void stop() {
        LOGGER.info("Stopped receiving mesasges from queue.");
        rabbitListenerEndpointRegistry.getListenerContainer("cameraMessageReceiver").stop();
    }
}
