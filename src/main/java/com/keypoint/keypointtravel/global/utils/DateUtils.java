package com.keypoint.keypointtravel.global.utils;

import java.sql.Date;
import java.time.LocalDate;
import org.springframework.stereotype.Component;

@Component
public class DateUtils {

    public static Date convertLocalDateToDate(LocalDate localDate) {
        if (localDate == null) {
            return null;
        }

        return Date.valueOf(localDate);
    }
}
