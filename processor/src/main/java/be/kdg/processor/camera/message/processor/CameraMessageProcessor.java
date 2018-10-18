package be.kdg.processor.camera.message.processor;

import be.kdg.processor.camera.CameraService;
import be.kdg.processor.camera.message.CameraMessage;
import be.kdg.processor.camera.message.receiver.Receiver;
import be.kdg.processor.fine.evaluation.FineEvaluationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Processes received camera messages.
 * Retrieves messages from the local/db buffer and validates them.
 * Note: Multiple receivers can be added to the processor.
 */
@RequiredArgsConstructor
@Service
public class CameraMessageProcessor implements Processor<CameraMessage> {
    private final List<FineEvaluationService> fineEvaluationServices;
    private final CameraService cameraService;
    private final Receiver<CameraMessage> receiver;

    //TODO: Possibility to switch between local/db buffer.
    @Override
    @Transactional
    public void process(int bufferTime) {
        cameraService.findAllCameraMessagesSince(LocalDateTime.now().minusSeconds(bufferTime))
                .ifPresent(messages -> messages.forEach(this::validate));
//        receiver.emptyMemoryBuffer();
    }

    /**
     * For each implementation of the FineEvaluationServie the cameraMessage is evaluated.
     */
    @Override
    public void validate(CameraMessage message) {
        fineEvaluationServices.forEach(evaluationService -> {
            evaluationService.checkForFine(message);
        });
    }
}

