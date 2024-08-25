package com.keypoint.keypointtravel.global.enumType.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum BadgeErrorCode implements ErrorCode {
    NOT_EXISTED_BADGE("001_NOT_EXISTED_GUIDE", "존재하지 않는 이용 가이드입니다."),
    DUPLICATED_BADGE_NAME("002_DUPLICATED_BADGE_NAME", "이미 존재하는 배지명입니다."),
    BADGE_NOT_OWNED("003_BADGE_NOT_OWNED", "보유한 배지가 아닙니다.");

    private final String code;
    private final String msg;
}
