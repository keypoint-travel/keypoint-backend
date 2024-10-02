package com.keypoint.keypointtravel.badge.dto.response.badgeInMember;

import com.keypoint.keypointtravel.badge.entity.Badge;
import com.keypoint.keypointtravel.badge.entity.EarnedBadge;
import com.keypoint.keypointtravel.global.utils.MessageSourceUtils;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class BadgeResponse {

    private Long badgeId;
    private String name;
    private int order;
    private String badgeImageUrl;
    private boolean isEarned;

    @QueryProjection
    public BadgeResponse(
        Badge badge,
        EarnedBadge earnedBadge,
        String activeImageUrl,
        String inactiveImageUrl
    ) {
        this.badgeId = badge.getId();
        this.order = badge.getOrder();
        this.name = MessageSourceUtils.getBadgeName(badge.getType());

        if (earnedBadge != null) {
            this.badgeImageUrl = activeImageUrl;
            this.isEarned = true;
        } else {
            this.badgeImageUrl = inactiveImageUrl;
            this.isEarned = false;
        }
    }
}
