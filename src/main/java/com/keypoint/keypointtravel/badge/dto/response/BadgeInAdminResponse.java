package com.keypoint.keypointtravel.badge.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class BadgeInAdminResponse {

    private Long badgeId;
    private String name;
    private int order;
    private String badgeOnImageUrl;
    private String badgeOffImageUrl;
}
