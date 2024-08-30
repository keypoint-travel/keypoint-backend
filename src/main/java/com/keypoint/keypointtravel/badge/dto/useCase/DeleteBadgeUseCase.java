package com.keypoint.keypointtravel.badge.dto.useCase;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DeleteBadgeUseCase {

    private Long[] badgeIds;

    public static DeleteBadgeUseCase from(Long[] badgeIds) {
        return new DeleteBadgeUseCase(badgeIds);
    }
}
