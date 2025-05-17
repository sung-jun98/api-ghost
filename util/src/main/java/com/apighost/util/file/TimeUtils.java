package com.apighost.util.file;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

/**
 * Utility class for handling and formatting time-related operations.
 * <p>
 * This class provides methods to get the current timestamp in a specific format and to convert a
 * {@link LocalDateTime} or millisecond-based timestamp into a formatted ISO-8601-like string.
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
    public static String getNow() {
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

    /**
     * Calculates the absolute time difference in milliseconds between two formatted time strings.
     *
     * <p>
     * The input strings must be in the format {@code "yyyy-MM-dd'T'HH:mm:ss.SSS"}, which is
     * consistent with ISO-8601-like timestamp formatting (e.g., {@code 2025-05-09T17:45:12.345}).
     * </p>
     *
     * @param time1 the first timestamp string
     * @param time2 the second timestamp string
     * @return the absolute difference between {@code time1} and {@code time2} in milliseconds
     *
     * <p><b>Example:</b></p>
     * <pre>{@code
     * long diff = TimeUtils.calculateDiffInMs("2025-05-09T17:45:12.000", "2025-05-09T17:45:13.500");
     * // diff == 1500
     * }</pre>
     */
    public static long calculateDiffInMs(String time1, String time2) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS");

        LocalDateTime dateTime1 = LocalDateTime.parse(time1, formatter);
        LocalDateTime dateTime2 = LocalDateTime.parse(time2, formatter);

        long epochMilli1 = dateTime1.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
        long epochMilli2 = dateTime2.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();

        return Math.abs(epochMilli1 - epochMilli2);
    }
}

