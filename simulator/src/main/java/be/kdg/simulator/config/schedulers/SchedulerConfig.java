package be.kdg.simulator.config.schedulers;

import be.kdg.simulator.config.GeneratorConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

@RequiredArgsConstructor
@Configuration
public class SchedulerConfig implements SchedulingConfigurer {

    @Autowired( required=false )
    private HighTrafficScheduler highTrafficScheduler;

    private final SimulationScheduler simulationScheduler;
    private final GeneratorConfig generatorConfig;

    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        simulationScheduler.startSimulation(generatorConfig.getFrequency());
        if (highTrafficScheduler != null) highTrafficScheduler.startCronJobs();
    }
}
