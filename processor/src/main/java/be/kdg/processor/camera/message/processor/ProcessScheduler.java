package be.kdg.processor.camera.message.processor;

import be.kdg.processor.camera.message.CameraMessage;
import be.kdg.processor.setting.SettingService;
import be.kdg.processor.shared.exception.ProcessorException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.stereotype.Component;

import java.util.concurrent.ScheduledFuture;

/**
 * Initiates the Processor, different processors can be added.
 */
@RequiredArgsConstructor
@Component
public class ProcessScheduler implements SchedulingConfigurer {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProcessScheduler.class);
    private static final String BUFFER_TIME_KEY = "message-buffer-time";
    private final Processor<CameraMessage> cameraMessageProcessor;
    private final TaskScheduler scheduler;
    private final SettingService settingService;
    private ScheduledFuture<?> scheduledFuture;
    private int bufferTime;


    /**
     * Initializes the scheduler.
     */
    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        updateBufferTime();
        startSchedule();
    }

    /**
     * Starts the schedule. If the bufferTime is changed, the schedule is reset with a new buffertime
     */
    private void startSchedule() {
        LOGGER.debug("Starting schedule empty buffer, with delay: " + bufferTime);
        scheduledFuture = scheduler.scheduleWithFixedDelay(() -> {
            if (updateBufferTime()) resetSchedule();
            else cameraMessageProcessor.process(bufferTime / 1000);
        }, bufferTime);
    }

    private void stopSchedule() {
        scheduledFuture.cancel(true);
    }

    private void resetSchedule() {
        stopSchedule();
        startSchedule();
    }

    /**
     * Updates the buffertime with the buffertime from the database.
     *
     * @return if the buffertime was changed in the database.
     */
    private boolean updateBufferTime() {
        try {
            int oldBufferTime = bufferTime;
            bufferTime = settingService.findByKey(BUFFER_TIME_KEY).getValue();
            return oldBufferTime != bufferTime;
        } catch (ProcessorException e) {
            LOGGER.error("Setting not found {}", BUFFER_TIME_KEY);
        }
        return true;
    }
}
