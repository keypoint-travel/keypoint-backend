package com.keypoint.keypointtravel.global.enumType.member;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum RoleType {
    ROLE_UNCERTIFIED_USER(0, "미인증 회원"),
    ROLE_CERTIFIED_USER(1, "인증 회원");

    private final int code;
    private final String description;
}
