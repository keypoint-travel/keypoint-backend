package com.keypoint.keypointtravel.badge.dto.useCase;

import com.keypoint.keypointtravel.global.enumType.setting.BadgeType;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BadgeTypeUseCase {
    private Long memberId;
    private BadgeType type;

    public static BadgeTypeUseCase of(
            Long memberId,
            BadgeType type
    ) {
        return new BadgeTypeUseCase(memberId, type);
    }
}
