package com.keypoint.keypointtravel.badge.service;

import com.keypoint.keypointtravel.badge.dto.response.badgeInMember.BadgeInMemberResponse;
import com.keypoint.keypointtravel.badge.dto.response.badgeInMember.BadgeResponse;
import com.keypoint.keypointtravel.badge.dto.response.badgeInMember.RepresentativeBadgeResponse;
import com.keypoint.keypointtravel.badge.respository.BadgeRepository;
import com.keypoint.keypointtravel.global.exception.GeneralException;
import com.keypoint.keypointtravel.member.dto.useCase.MemberIdUseCase;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberBadgeService {

    private final BadgeRepository badgeRepository;


    /**
     * 배지 페이지 배지 정보 조회
     *
     * @param useCase
     * @return
     */
    public BadgeInMemberResponse findBadgesInMember(MemberIdUseCase useCase) {
        try {
            BadgeInMemberResponse response = new BadgeInMemberResponse();
            RepresentativeBadgeResponse representativeBadge = badgeRepository.findRepresentativeBadgeByUserId(
                useCase.getMemberId()
            );
            List<BadgeResponse> badges = badgeRepository.findBadgesByUserId(useCase.getMemberId());

            response.setRepresentativeBadge(representativeBadge);
            response.setBadges(badges);
            return response;
        } catch (Exception ex) {
            throw new GeneralException(ex);
        }
    }
}
