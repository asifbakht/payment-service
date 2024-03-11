package com.social.auth.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalAmount;

import static com.social.auth.utils.Constants.ZONE_UTC;
import static java.time.format.DateTimeFormatter.ISO_LOCAL_DATE_TIME;

public class Helper {

    public static String getProcessingDateTime(final int processInDays) {
        return LocalDateTime
                .of(
                        LocalDate
                                .now(ZoneId.of(ZONE_UTC))
                                .plusDays(processInDays),
                        LocalTime.MIDNIGHT)
                .format(ISO_LOCAL_DATE_TIME);

    }

}
