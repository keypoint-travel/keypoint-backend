package com.keypoint.keypointtravel.badge.respository;

import com.keypoint.keypointtravel.badge.dto.response.badgeInMember.BadgeResponse;
import com.keypoint.keypointtravel.badge.dto.response.badgeInMember.RepresentativeBadgeResponse;
import com.keypoint.keypointtravel.member.dto.response.otherMemberProfile.EarnedBadgeResponse;

import java.util.List;

public interface MemberBadgeCustomRepository {

    List<BadgeResponse> findBadgesByUserId(Long memberId);

    List<EarnedBadgeResponse> findEarnedBadgeByUserId(Long memberId);

    RepresentativeBadgeResponse findRepresentativeBadgeByUserId(Long memberId);
}
