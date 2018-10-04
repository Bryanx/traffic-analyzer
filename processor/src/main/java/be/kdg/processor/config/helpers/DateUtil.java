package be.kdg.processor.config.helpers;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Component
public class DateUtil {
    public double getMillisBetweenDates(LocalDateTime date1, LocalDateTime date2) {
        return date1.until(date2, ChronoUnit.MILLIS);
    }
}
