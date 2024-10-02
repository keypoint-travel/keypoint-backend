package com.keypoint.keypointtravel.global.enumType.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ThemeErrorCode implements ErrorCode {
    NOT_EXISTED_Theme("001_NOT_EXISTED_Theme", "존재하지 않는 테마 입니다."),
    NOT_EXISTED_Member_Theme("002_NOT_EXISTED_Member_Theme", "해당 유저의 테마가 지정되어있지 않습니다.");

    private final String code;
    private final String msg;
}