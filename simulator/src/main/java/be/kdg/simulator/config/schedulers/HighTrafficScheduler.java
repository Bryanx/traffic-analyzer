package be.kdg.simulator.config.schedulers;

import be.kdg.simulator.config.GeneratorConfig;
import be.kdg.simulator.config.converters.CronConverter;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;

/**
 * Schedules high traffic periods based on each busy period in the application.properties file.
 */
@ConditionalOnProperty(name = "generator.type", havingValue = "random")
@RequiredArgsConstructor
@Component
public class HighTrafficScheduler {
    public static final Logger LOGGER = LoggerFactory.getLogger(HighTrafficScheduler.class);
    private static final long BUSY_PERIOD_FREQUENCY = 500;
    private final SimulationScheduler simulationScheduler;
    private final GeneratorConfig generatorConfig;
    private final TaskScheduler scheduler;
    private final CronConverter cronConverter;

    public void startCronJobs() {
        for (String busyPeriod : generatorConfig.getBusyperiod()) {
            String[] splittedBusyPeriod = busyPeriod.split("-");
            String startTime = cronConverter.convertToCron(splittedBusyPeriod[0]);
            String endTime = cronConverter.convertToCron(splittedBusyPeriod[1]);
            scheduleJob(startTime, BUSY_PERIOD_FREQUENCY, "High traffic period started.");
            scheduleJob(endTime, generatorConfig.getFrequency(), "High traffic period ended.");
        }
    }

    private void scheduleJob(String time, long newFrequency, String comment) {
        scheduler.schedule(() -> {
            LOGGER.info(comment);
            simulationScheduler.resetSimulation(newFrequency);
        }, new CronTrigger(time));
    }
}
