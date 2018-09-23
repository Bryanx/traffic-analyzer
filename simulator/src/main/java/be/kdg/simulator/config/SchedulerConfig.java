package be.kdg.simulator.config;

import be.kdg.simulator.Simulator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.support.CronTrigger;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledFuture;

@Configuration
public class SchedulerConfig implements SchedulingConfigurer {
    private static final long BUSY_PERIOD_FREQUENCY = 100;
    private static final int POOL_SIZE = 100;
    private final Simulator simulator;
    private final GeneratorConfig generatorConfig;
    private final TaskScheduler scheduler;
    private ScheduledFuture<?> scheduledSimulation;

    public SchedulerConfig(TaskScheduler scheduler, Simulator simulator, GeneratorConfig generatorConfig) {
        this.scheduler = scheduler;
        this.simulator = simulator;
        this.generatorConfig = generatorConfig;
    }

    /**
     * Configure all scheduled tasks. This triggers the new configured jobs.
     */
    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        startSimulation(generatorConfig.getFrequency());
        if (generatorConfig.getBusyperiod() != null) {
            startCronJob();
        }
    }

    @Bean
    public Executor taskExecutor() {
        return Executors.newScheduledThreadPool(POOL_SIZE);
    }

    /**
     * Starts the High traffic period based on each busy period in application.properties
     */
    private void startCronJob() {
        for (String busyPeriod : generatorConfig.getBusyperiod()) {
            scheduler.schedule(() -> resetSimulation(BUSY_PERIOD_FREQUENCY, "High traffic period started."),
                    new CronTrigger(generatorConfig.getStartBusyPeriodCronFormat(busyPeriod)));
            scheduler.schedule(() -> resetSimulation(generatorConfig.getFrequency(), "High traffic period ended."),
                    new CronTrigger(generatorConfig.getEndBusyPeriodCronFormat(busyPeriod)));

        }
    }

    /**
     * Stop and start the simulation with a new frequency
     * @param newFrequency The new frequency of the delay between each simulated message
     * @param logMessage A log message to note what was changed.
     */
    private void resetSimulation(long newFrequency, String logMessage) {
        System.out.println(logMessage);
        stopSimulation();
        startSimulation(newFrequency);
    }

    private void startSimulation(long frequency) {
        scheduledSimulation = scheduler.scheduleWithFixedDelay(simulator::startSimulation, frequency);
    }

    private void stopSimulation() {
        scheduledSimulation.cancel(true);
    }
}