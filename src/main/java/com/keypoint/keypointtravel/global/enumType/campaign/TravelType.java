package com.keypoint.keypointtravel.global.enumType.campaign;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TravelType {

    ROUND_TRIP(0, "왕복"),
    ONE_WAY(1, "편도"),
    MULTI_STOP(2, "다구간");

    private final int code;
    private final String description;
}
