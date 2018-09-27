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
    private final TaskScheduler scheduler;
    private ScheduledFuture<?> scheduledSimulation;


    public void startSimulation(long frequency) {
        scheduledSimulation = scheduler.scheduleWithFixedDelay(simulator::startSimulation, frequency);
    }

    public void stopSimulation() {
        scheduledSimulation.cancel(true);
    }

    public void resetSimulation(long newFrequency) {
        stopSimulation();
        startSimulation(newFrequency);
    }
}
