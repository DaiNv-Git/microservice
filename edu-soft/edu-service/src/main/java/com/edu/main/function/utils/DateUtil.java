package com.edu.main.function.utils;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class DateUtil {

    private static final ZoneId ZONE_HCM = TimeZone.getTimeZone("Asia/Ho_Chi_Minh").toZoneId();

    public static LocalDateTime getStartDateForToday(Date startDate) {
        LocalDateTime startTime = LocalDateTime.now();
        LocalDateTime ldt = LocalDateTime.ofInstant(startDate.toInstant(), ZONE_HCM);
        startTime = startTime.withHour(ldt.getHour());
        startTime = startTime.withMinute(ldt.getMinute());
        return startTime;
    }

    public static Date setTimeForDate(Date date, String time) {
        String[] times = time.trim().split(":");
        if (times == null || times.length < 3) {
            throw new IllegalArgumentException("Time to set for date invalid!");
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR, Integer.parseInt(times[0]));
        calendar.set(Calendar.MINUTE, Integer.parseInt(times[1]));
        calendar.set(Calendar.SECOND, Integer.parseInt(times[2]));
        return calendar.getTime();
    }
}
