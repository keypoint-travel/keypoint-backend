package com.keypoint.keypointtravel.global.enumType.campaign;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Status {

    IN_PROGRESS(0, "진행 중"),
    FINISHED(1, "완료");

    private final int code;
    private final String description;
}
