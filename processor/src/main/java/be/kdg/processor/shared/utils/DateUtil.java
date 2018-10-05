package be.kdg.processor.shared.utils;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Component
public class DateUtil {

    public static final int MILIS_PER_HOUR = 3600000;

    public double getHoursBetweenDates(LocalDateTime date1, LocalDateTime date2) {
        double milis;
        if (date1.isBefore(date2)) {
            milis = date1.until(date2, ChronoUnit.MILLIS);
        } else {
            milis = date2.until(date1, ChronoUnit.MILLIS);
        }
        return milis / MILIS_PER_HOUR;

    }
}
