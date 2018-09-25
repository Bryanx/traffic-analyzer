package be.kdg.simulator.config.converters;

import org.springframework.stereotype.Component;

@Component
public class CronConverter {

    public String convertTimeToCron(String time) {
        String[] splittedTime = time.split(":");
        return String.format("0 %s %s * * *", splittedTime[1], splittedTime[0]);
    }
}
