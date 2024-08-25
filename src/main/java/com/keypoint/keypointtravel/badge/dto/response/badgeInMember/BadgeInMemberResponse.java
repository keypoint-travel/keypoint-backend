package com.keypoint.keypointtravel.badge.dto.response.badgeInMember;

import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class BadgeInMemberResponse {

    private RepresentativeBadgeResponse representativeBadge;
    private List<BadgeResponse> badges;

    public void setRepresentativeBadge(RepresentativeBadgeResponse representativeBadge) {
        this.representativeBadge = representativeBadge;
    }

    public void setBadges(List<BadgeResponse> badges) {
        this.badges = badges;
    }
}
