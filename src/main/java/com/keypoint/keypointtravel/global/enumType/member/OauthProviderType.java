package com.keypoint.keypointtravel.global.enumType.member;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum OauthProviderType {
    NONE(1, "자체 로그인"),
    GOOGLE(2, "구글 로그인"),
    APPLE(3, "애플 로그인");

    private final int code;
    private final String value;
}
