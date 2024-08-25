package com.keypoint.keypointtravel.badge.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BadgeIdRequest {

    private Long badgeId;

    public static BadgeIdRequest from(Long badgeId) {
        return new BadgeIdRequest(badgeId);
    }
}
