package com.keypoint.keypointtravel.global.enumType.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum NoticeErrorCode implements ErrorCode {
    EXISTS_NOTICE_CONTENT("002_DUPLICATED_TRANSLATION_LANGUAGE", "이미 해당 언어로 공지 내용이 존재합니다."),
    NOT_EXISTED_NOTICE("002_NOT_EXISTED_NOTICE", "존재하지 않는 공지사항 입니다.");

    private final String code;
    private final String msg;
}