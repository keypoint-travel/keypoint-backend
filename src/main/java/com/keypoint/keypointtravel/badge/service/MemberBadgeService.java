package com.keypoint.keypointtravel.badge.service;

import com.keypoint.keypointtravel.badge.dto.response.badgeInMember.BadgeInMemberResponse;
import com.keypoint.keypointtravel.badge.dto.response.badgeInMember.BadgeResponse;
import com.keypoint.keypointtravel.badge.dto.response.badgeInMember.RepresentativeBadgeResponse;
import com.keypoint.keypointtravel.badge.dto.useCase.BadgeIdUseCase;
import com.keypoint.keypointtravel.badge.entity.Badge;
import com.keypoint.keypointtravel.badge.entity.EarnedBadge;
import com.keypoint.keypointtravel.badge.respository.BadgeRepository;
import com.keypoint.keypointtravel.badge.respository.EarnedBadgeRepository;
import com.keypoint.keypointtravel.global.enumType.error.BadgeErrorCode;
import com.keypoint.keypointtravel.global.enumType.setting.BadgeType;
import com.keypoint.keypointtravel.global.exception.GeneralException;
import com.keypoint.keypointtravel.global.utils.LogUtils;
import com.keypoint.keypointtravel.member.dto.useCase.MemberIdUseCase;
import com.keypoint.keypointtravel.member.entity.Member;
import com.keypoint.keypointtravel.member.repository.member.MemberRepository;
import com.keypoint.keypointtravel.member.repository.memberDetail.MemberDetailRepository;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberBadgeService {

    private final MemberRepository memberRepository;
    private final BadgeRepository badgeRepository;
    private final EarnedBadgeRepository earnedBadgeRepository;
    private final MemberDetailRepository memberDetailRepository;

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

    /**
     * 대표 배지 설정 변경
     *
     * @param useCase
     */
    @Transactional
    public void updateRepresentativeBadge(BadgeIdUseCase useCase) {
        try {
            // 1. 보유한 배지인지 확인
            Optional<EarnedBadge> earnedBadgeOptional = earnedBadgeRepository.findByMemberIdAndBadgeId(
                useCase.getMemberId(),
                useCase.getBadgeId()
            );
            if (earnedBadgeOptional.isEmpty()) {
                throw new GeneralException(BadgeErrorCode.BADGE_NOT_OWNED);
            }

            // 2. 대표 배지 변경
            memberDetailRepository.updateRepresentativeBadge(
                useCase.getMemberId(),
                earnedBadgeOptional.get().getBadge()
            );
        } catch (Exception ex) {
            throw new GeneralException(ex);
        }
    }

    /**
     * 배지 발급하는 함수
     *
     * @param memberId    발급받을 회원 아이디
     * @param badgeType 발급받을 배지
     * @return 발급 여부
     */
    public boolean earnBadge(Long memberId, BadgeType badgeType) {
        try {
            // 이미 발급받은 배지인지 확인
            Member member = memberRepository.getReferenceById(memberId);
            if (earnedBadgeRepository.existsByMemberIdAndBadgeType(member.getId(), badgeType)) {
                return false;
            }

            // 배지 발급
            Badge badge = badgeRepository.findByBadgeType(badgeType);
            EarnedBadge earnedBadge = EarnedBadge.of(member, badge);
            earnedBadgeRepository.save(earnedBadge);
            return true;
        } catch (Exception ex) {
            LogUtils.writeErrorLog("earnBadge",
                String.format("Fail to earn badge {0} {1}", badgeType.name(), memberId.toString()));
            return false;
        }
    }
}
