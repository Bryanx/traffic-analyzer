package be.kdg.simulator.config;

import be.kdg.simulator.Simulator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.support.CronTrigger;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledFuture;

/**
 * Creates all scheduled tasks.
 */
@Configuration
public class SchedulerConfig implements SchedulingConfigurer {
    private static final long BUSY_PERIOD_FREQUENCY = 1000;
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

    @Bean
    public Executor taskExecutor() {
        return Executors.newScheduledThreadPool(POOL_SIZE);
    }

    /**
     * Configure all scheduled tasks. This triggers the new configured jobs.
     */
    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        startSimulation(generatorConfig.getFrequency());
        startCronJob();
    }

    /**
     * Starts the High traffic period based on each busy period in application.properties
     */
    private void startCronJob() {
        for (String busyPeriod : generatorConfig.getBusyperiod()) {
            resetSimulation(BUSY_PERIOD_FREQUENCY, "High traffic period started.", busyPeriod.split("-")[0]);
            resetSimulation(generatorConfig.getFrequency(), "High traffic period ended.", busyPeriod.split("-")[1]);
        }
    }

    /**
     * Stop and start the simulation with a new frequency
     * @param newFrequency The new frequency of the delay between each simulated message
     */
    private void resetSimulation(long newFrequency, String message, String timeToReset) {
        scheduler.schedule(() -> {
            System.out.println(message);
            stopSimulation();
            startSimulation(newFrequency);
        }, new CronTrigger(convertTimeToCron(timeToReset)));
    }

    private void startSimulation(long frequency) {
        scheduledSimulation = scheduler.scheduleWithFixedDelay(simulator::startSimulation, frequency);
    }

    private void stopSimulation() {
        scheduledSimulation.cancel(true);
    }

    /**
     * @param time in format e.g. 12:30, 14:10
     */
    private String convertTimeToCron(String time) {
        String[] splittedTime = time.split(":");
        return String.format("0 %s %s * * *", splittedTime[1], splittedTime[0]);
    }
}