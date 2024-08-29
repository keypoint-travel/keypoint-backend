package com.keypoint.keypointtravel.global.enumType.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum NoticeErrorCode implements ErrorCode {
    EXISTS_NOTICE_CONTENT("001_EXISTS_NOTICE_CONTENT", "이미 해당 언어로 공지 내용이 존재합니다.");

    private final String code;
    private final String msg;
}