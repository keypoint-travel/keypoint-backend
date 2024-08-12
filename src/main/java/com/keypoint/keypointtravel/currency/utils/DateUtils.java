package com.keypoint.keypointtravel.currency.utils;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DateUtils {

    public static String getToday() {
        // 현재 날짜
        LocalDate currentDate = LocalDate.now();
        // 날짜 포맷
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        // 토요일일 경우 금요일로 변경
        if (currentDate.getDayOfWeek() == DayOfWeek.SATURDAY) {
            return currentDate.minusDays(1).format(formatter);
        }
        // 일요일일 경우 금요일로 변경
        if (currentDate.getDayOfWeek() == DayOfWeek.SUNDAY) {
            return currentDate.minusDays(2).format(formatter);
        }
        return currentDate.format(formatter);
    }
}
