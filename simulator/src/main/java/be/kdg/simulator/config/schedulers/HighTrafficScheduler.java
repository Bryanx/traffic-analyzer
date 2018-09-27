package be.kdg.simulator.config.schedulers;

import be.kdg.simulator.config.GeneratorConfig;
import be.kdg.simulator.config.converters.CronConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;

@ConditionalOnProperty(name = "generator.type", havingValue = "random")
@RequiredArgsConstructor
@Component
public class HighTrafficScheduler {
    private static final int BUSY_PERIOD_FREQUENCY = 500;
    private final SimulationScheduler simulationScheduler;
    private final GeneratorConfig generatorConfig;
    private final TaskScheduler scheduler;
    private final CronConverter cronConverter;

    /**
     * Starts the High traffic period based on each busy period in application.properties
     */
    public void startCronJobs() {
        for (String busyPeriod : generatorConfig.getBusyperiod()) {

            String[] splittedBusyPeriod = busyPeriod.split("-");
            String startTime = cronConverter.convertTimeToCron(splittedBusyPeriod[0]);
            String endTime = cronConverter.convertTimeToCron(splittedBusyPeriod[1]);

            scheduler.schedule(() -> {
                System.out.println("High traffic period started.");
                simulationScheduler.resetSimulation(BUSY_PERIOD_FREQUENCY);
            }, new CronTrigger(startTime));

            scheduler.schedule(() -> {
                System.out.println("High traffic period ended.");
                simulationScheduler.resetSimulation(generatorConfig.getFrequency());
            }, new CronTrigger(endTime));

        }
    }
}
