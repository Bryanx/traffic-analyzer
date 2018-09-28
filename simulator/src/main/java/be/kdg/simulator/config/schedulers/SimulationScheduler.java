package be.kdg.simulator.config.schedulers;

import be.kdg.simulator.Simulator;
import be.kdg.simulator.config.converters.CronConverter;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.concurrent.ScheduledFuture;

@RequiredArgsConstructor
@Component
public class SimulationScheduler {
    private static final Logger LOGGER = LoggerFactory.getLogger(SimulationScheduler.class);
    private final Simulator simulator;
    private final TaskScheduler scheduler;
    private ScheduledFuture<?> scheduledSimulation;


    public void startSimulation(long frequency) {
        LOGGER.info("Starting simulation with frequency: " + frequency);
        scheduledSimulation = scheduler.scheduleWithFixedDelay(simulator::startSimulation, frequency);
    }

    public void stopSimulation() {
        scheduledSimulation.cancel(true);
    }

    public void resetSimulation(long newFrequency) {
        stopSimulation();
        startSimulation(newFrequency);
    }

    /**
     * Wait a certain period of time before starting the simulation again.
     * @param delay in ms
     */
    public void resetSimulationWithDelay(long delay) {
        stopSimulation();
        Date scheduleDate = new Date(System.currentTimeMillis() + delay);
        scheduler.schedule(simulator::startSimulation, scheduleDate);
        LOGGER.info("New simulation scheduled for: " + scheduleDate);
    }
}
