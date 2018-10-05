package be.kdg.processor.shared.utils;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;

import static org.junit.Assert.*;
\
@RunWith(SpringRunner.class)
@SpringBootTest
public class DateUtilTest {

    @Autowired
    DateUtil dateUtil;

    @Test
    public void getHoursBetweenDates() {
        LocalDateTime date1 = LocalDateTime.of(2000,1,1,15,0);
        LocalDateTime date2 = LocalDateTime.of(2000,1,1,16,30);
        assertEquals(1.5, dateUtil.getHoursBetweenDates(date1, date2), 0.0);
    }

}