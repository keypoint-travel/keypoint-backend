package com.keypoint.keypointtravel.badge.dto.useCase;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BadgeIdUseCase {

    private Long memberId;
    private Long badgeId;

    public BadgeIdUseCase(Long badgeId) {
        this.badgeId = badgeId;
    }

    public static BadgeIdUseCase from(Long badgeId) {
        return new BadgeIdUseCase(badgeId);
    }

    public static BadgeIdUseCase of(Long memberId, Long badgeId) {
        return new BadgeIdUseCase(memberId, badgeId);
    }
}
