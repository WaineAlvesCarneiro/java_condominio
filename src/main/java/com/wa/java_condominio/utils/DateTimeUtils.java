package com.wa.java_condominio.utils;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public class DateTimeUtils {

    public static LocalDateTime toUTC(LocalDateTime localDateTime) {
        if (localDateTime == null) {
            return null;
        }
        ZonedDateTime zonedLocal = localDateTime.atZone(ZoneId.systemDefault());
        ZonedDateTime zonedUtc = zonedLocal.withZoneSameInstant(ZoneId.of("UTC"));
        return zonedUtc.toLocalDateTime();
    }
    
    public static LocalDateTime fromUTC(LocalDateTime utcDateTime) {
        if (utcDateTime == null) {
            return null;
        }
        ZonedDateTime zonedUtc = utcDateTime.atZone(ZoneId.of("UTC"));
        ZonedDateTime zonedLocal = zonedUtc.withZoneSameInstant(ZoneId.systemDefault());
        return zonedLocal.toLocalDateTime();
    }
}
