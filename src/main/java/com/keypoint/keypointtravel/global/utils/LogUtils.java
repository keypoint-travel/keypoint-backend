package com.keypoint.keypointtravel.global.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class LogUtils {

    public static void writeInfoLog(String header, String message) {
        log.info(String.format("[%s] %s", header, message));
    }
}
