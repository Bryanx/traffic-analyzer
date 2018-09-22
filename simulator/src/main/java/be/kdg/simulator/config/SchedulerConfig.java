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
     * Trigger the start of the jobs.
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
        return Executors.newScheduledThreadPool(100);
    }

    /**
     * Starts the High traffic period based on the busy period config.
     */
    public void startCronJob() {
        scheduler.schedule(() -> {
            System.out.println("High traffic period started.");
            stopSimulation();
            startSimulation(generatorConfig.getPeakfrequency());
        }, new CronTrigger(generatorConfig.getStartBusyPeriodCronFormat()));
        scheduler.schedule(() -> {
            System.out.println("High traffic period ended.");
            stopSimulation();
            startSimulation(generatorConfig.getFrequency());
        }, new CronTrigger(generatorConfig.getEndBusyPeriodCronFormat()));
    }

    public void startSimulation(long frequency) {
        scheduledSimulation = scheduler.scheduleWithFixedDelay(simulator::startSimulation, frequency);
    }

    public void stopSimulation() {
        scheduledSimulation.cancel(true);
    }
}