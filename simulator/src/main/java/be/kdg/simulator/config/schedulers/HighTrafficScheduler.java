package be.kdg.simulator.config.schedulers;

import be.kdg.simulator.config.GeneratorConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.stereotype.Component;


@RequiredArgsConstructor
@Component
public class HighTrafficScheduler implements SchedulingConfigurer {
    private static final int BUSY_PERIOD_FREQUENCY = 500;
    private final SimulationScheduler simulationScheduler;
    private final GeneratorConfig generatorConfig;

    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        simulationScheduler.startSimulation(generatorConfig.getFrequency());
        startCronJobs();
    }

    /**
     * Starts the High traffic period based on each busy period in application.properties
     */
    private void startCronJobs() {
        for (String busyPeriod : generatorConfig.getBusyperiod()) {
            simulationScheduler.resetSimulation(BUSY_PERIOD_FREQUENCY, "High traffic period started.", busyPeriod.split("-")[0]);
            simulationScheduler.resetSimulation(generatorConfig.getFrequency(), "High traffic period ended.", busyPeriod.split("-")[1]);
        }
    }
}
