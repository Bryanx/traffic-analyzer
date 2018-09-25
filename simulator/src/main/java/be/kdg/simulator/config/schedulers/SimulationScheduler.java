package be.kdg.simulator.config.schedulers;

import be.kdg.simulator.Simulator;
import be.kdg.simulator.config.converters.CronConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;

import java.util.concurrent.ScheduledFuture;

@RequiredArgsConstructor
@Component
public class SimulationScheduler {
    private final Simulator simulator;
    private final CronConverter cronConverter;
    private final TaskScheduler scheduler;
    private ScheduledFuture<?> scheduledSimulation;

    /**
     * Stop and start the simulation with a new frequency
     * @param newFrequency The new frequency of the delay between each simulated message
     */
    public void resetSimulation(long newFrequency, String message, String timeToReset) {
        scheduler.schedule(() -> {
            System.out.println(message);
            stopSimulation();
            startSimulation(newFrequency);
        }, new CronTrigger(cronConverter.convertTimeToCron(timeToReset)));
    }

    public void startSimulation(long frequency) {
        scheduledSimulation = scheduler.scheduleWithFixedDelay(simulator::startSimulation, frequency);
    }

    public void stopSimulation() {
        scheduledSimulation.cancel(true);
    }
}
