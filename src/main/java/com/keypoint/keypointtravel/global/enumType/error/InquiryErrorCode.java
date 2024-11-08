package com.keypoint.keypointtravel.global.enumType.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum InquiryErrorCode implements ErrorCode {
    TOO_MANY_IMAGES("001_TOO_MANY_IMAGES", "이미지는 5개까지 추가할 수 없습니다.");

    private final String code;
    private final String msg;
}
