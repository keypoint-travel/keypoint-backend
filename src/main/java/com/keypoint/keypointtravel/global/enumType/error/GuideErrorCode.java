package com.keypoint.keypointtravel.global.enumType.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum GuideErrorCode implements ErrorCode {
    DUPLICATED_ORDER("001_DUPLICATED_ORDER",
        "이미 존재하는 순서 번호 입니다. 해당 순서 번호를 가진 이용 가이드를 수정하거나 다른 순서 번호로 등록해주세요."),
    NOT_EXISTED_GUIDE("001_NOT_EXISTED_GUIDE", "존재하지 않는 이용 가이드입니다."),
    DUPLICATED_GUIDE_TRANSLATION_LANGUAGE("002_DUPLICATED_GUIDE_TRANSLATION_LANGUAGE",
        "이미 존재하는 언어 버전입니다.");

    private final String code;
    private final String msg;
}
