package com.keypoint.keypointtravel.global.enumType.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum GuideErrorCode implements ErrorCode {
    NOT_EXISTED_GUIDE("001_NOT_EXISTED_GUIDE", "존재하지 않는 이용 가이드입니다."),
    DUPLICATED_GUIDE_TRANSLATION_LANGUAGE("002_DUPLICATED_TRANSLATION_LANGUAGE",
        "이미 존재하는 언어 버전입니다.");

    private final String code;
    private final String msg;
}
