package com.keypoint.keypointtravel.global.enumType.member;

import java.util.Arrays;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum RoleType {
    ROLE_UNCERTIFIED_USER(0, "미인증 회원"),
    ROLE_CERTIFIED_USER(1, "인증 회원"),
    ROLE_ADMIN(2, "관리자"),
    ROLE_PENDING_WITHDRAWAL(3, "탈퇴 대기"),

    ROLE_NONE(100, "알수 없는 권한");

    private final int code;
    private final String description;

    public static RoleType fromName(String name) {
        return Arrays.stream(RoleType.values())
            .filter(t -> t.name().equals(name))
            .findFirst()
            .orElse(RoleType.ROLE_NONE);
    }
}
