package com.keypoint.keypointtravel.global.enumType.member;

import com.keypoint.keypointtravel.member.entity.QMemberDetail;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.EnumPath;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberExpression;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum GenderType {
    NONE(1, "설정 하지 않음"),
    MAN(2, "남성"),
    WOMAN(3, "여성");

    private final int code;
    private final String value;

    public static BooleanExpression containsEnumValue(EnumPath<GenderType> path, String keyword) {
        List<GenderType> types = new ArrayList<>();

        for (GenderType genderType : GenderType.values()) {
            if (genderType.getValue().contains(keyword)) {
                types.add(genderType);
            }
        }

        return path.in(types);
    }

    public static NumberExpression getOrderSpecifier() {
        QMemberDetail memberDetail = QMemberDetail.memberDetail;

        return Expressions.cases()
            .when(memberDetail.gender.eq(GenderType.MAN)).then(1)
            .when(memberDetail.gender.eq(GenderType.NONE)).then(2)
            .when(memberDetail.gender.eq(GenderType.WOMAN)).then(3)
            .otherwise(4);
    }
}
