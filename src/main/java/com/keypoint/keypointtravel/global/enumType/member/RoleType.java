package com.keypoint.keypointtravel.global.enumType.member;

import com.keypoint.keypointtravel.member.entity.QMember;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.EnumPath;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberExpression;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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

    public static BooleanExpression containsEnumValue(EnumPath<RoleType> path, String keyword) {
        List<RoleType> types = new ArrayList<>();
        for (RoleType genderType : RoleType.values()) {
            if (genderType.getDescription().contains(keyword)) {
                types.add(genderType);
            }
        }

        return path.in(types);
    }

    public static NumberExpression getOrderSpecifier() {
        QMember member = QMember.member;

        return Expressions.cases()
            .when(member.role.eq(RoleType.ROLE_ADMIN)).then(1)
            .when(member.role.eq(RoleType.ROLE_UNCERTIFIED_USER)).then(2)
            .when(member.role.eq(RoleType.ROLE_NONE)).then(3)
            .when(member.role.eq(RoleType.ROLE_CERTIFIED_USER)).then(4)
            .when(member.role.eq(RoleType.ROLE_PENDING_WITHDRAWAL)).then(5)
            .otherwise(6);
    }
}
