package com.keypoint.keypointtravel.global.enumType.notification;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AlarmType {
    PUSH(1, "푸시 알림"),
    MARKETING(2, "마켓팅 알림");

    private final int code;
    private final String description;
}
