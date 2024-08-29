package com.keypoint.keypointtravel.badge.respository;

import com.keypoint.keypointtravel.badge.dto.response.badgeInMember.BadgeResponse;
import com.keypoint.keypointtravel.badge.dto.response.badgeInMember.RepresentativeBadgeResponse;
import java.util.List;

public interface MemberBadgeCustomRepository {

    List<BadgeResponse> findBadgesByUserId(Long memberId);

    RepresentativeBadgeResponse findRepresentativeBadgeByUserId(Long memberId);
}
