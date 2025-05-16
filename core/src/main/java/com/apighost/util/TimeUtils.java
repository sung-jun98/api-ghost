package com.apighost.util;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

/**
 * Utility class for handling and formatting time-related operations.
 * <p>
 * This class provides methods to get the current timestamp in a specific format
 * and to convert a {@link LocalDateTime} or millisecond-based timestamp into
 * a formatted ISO-8601-like string.
 * </p>
 *
 * @author kobenlys
 * @version BETA-0.0.1
 */
public class TimeUtils {

    /**
     * Returns the current system time formatted as "yyyy-MM-dd'T'HH:mm:ss.SSS".
     *
     * @return the formatted current time string
     * <p>Example: 2025-05-09T17:45:12.345</p>
     */
    public static String getNow(){
        return convertFormat(LocalDateTime.now());
    }

    /**
     * Returns the current system time formatted as "yyyy-MM-dd'T'HH:mm:ss.SSS".
     *
     * @return the formatted current time string
     */
    public static String convertFormat(LocalDateTime localDateTime) {
        return localDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS"));
    }

    /**
     * Converts a millisecond-based timestamp to a formatted string
     *
     * @param timeMs the timestamp in milliseconds since the epoch (UTC)
     * @return the formatted time string
     * <p>Example: 2025-05-09T17:45:12.345</p>
     */
    public static String convertFormat(Long timeMs) {
        LocalDateTime dateTime = Instant.ofEpochMilli(timeMs)
            .atZone(ZoneId.systemDefault())
            .toLocalDateTime();
        return dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS"));
    }
}

