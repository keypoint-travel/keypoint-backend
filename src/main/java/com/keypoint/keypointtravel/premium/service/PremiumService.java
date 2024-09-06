package com.keypoint.keypointtravel.premium.service;

import com.keypoint.keypointtravel.global.enumType.error.PremiumErrorCode;
import com.keypoint.keypointtravel.global.exception.GeneralException;
import com.keypoint.keypointtravel.member.entity.Member;
import com.keypoint.keypointtravel.member.repository.member.MemberRepository;
import com.keypoint.keypointtravel.premium.dto.response.RemainingPeriodResponse;
import com.keypoint.keypointtravel.premium.dto.useCase.PremiumMemberUseCase;
import com.keypoint.keypointtravel.premium.entity.MemberPremium;
import com.keypoint.keypointtravel.premium.repository.MemberPremiumRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PremiumService {

    private final MemberRepository memberRepository;
    private final MemberPremiumRepository memberPremiumRepository;

    /**
     * 남은 프리미엄 기간 조회 함수
     *
     * @Param memberId useCase
     */
    @Transactional
    public RemainingPeriodResponse findRemainingPremiumDays(PremiumMemberUseCase useCase) {
        MemberPremium memberPremium = memberPremiumRepository.findByMemberId(useCase.getMemberId())
            .orElseThrow(() -> new GeneralException(PremiumErrorCode.NOT_FOUND_MEMBER_PREMIUM));
        Long remainingDays = ChronoUnit.DAYS.between(memberPremium.getStartedAt(), memberPremium.getExpirationAt());
        return new RemainingPeriodResponse(remainingDays);
    }

    /**
     * 7일 무료 프리미엄 적용 함수
     *
     * @Param memberId useCase
     */
    @Transactional
    public void startFreeTrial(PremiumMemberUseCase premiumMemberUseCase) {
        // 프리미엄 기록이 있는지 확인, 있다면 예외 처리
        if (memberPremiumRepository.existsByMemberId(premiumMemberUseCase.getMemberId())) {
            throw new GeneralException(PremiumErrorCode.ALREADY_EXIST_MEMBER_PREMIUM);
        }
        Member member = memberRepository.getReferenceById(premiumMemberUseCase.getMemberId());
        // 새로운 프리미엄 생성(7일)
        MemberPremium newMemberPremium = MemberPremium.builder()
            .member(member)
            .expirationAt(LocalDateTime.now().plusDays(7))
            .isActive(true)
            .isFree(true)
            .build();
        memberPremiumRepository.save(newMemberPremium);
    }

    /**
     * 유료 프리미엄 회원 업데이트 함수
     *
     * @Param memberId useCase
     */
    @Transactional
    public void updateMemberPremium(Long memberId) {
        Member member = memberRepository.getReferenceById(memberId);
        Optional<MemberPremium> memberPremium = memberPremiumRepository.findByMemberId(memberId);
        // 이미 적용했던 기록이 있는 경우
        if (memberPremium.isPresent()) {
            updateExistingMemberPremium(memberPremium.get());
            return;
        }
        // 처음 프리미엄을 적용할 경우
        createNewMemberPremium(member);
    }

    private void updateExistingMemberPremium(MemberPremium memberPremium) {
        // 7일간 무료권 적용중인 경우
        if (memberPremium.isFree()) {
            updateFreeToPremium(memberPremium);
            return;
        }
        // 유료 프리미엄이 적용중인 경우
        if (memberPremium.getExpirationAt().isAfter(LocalDateTime.now())
            && memberPremium.isActive()) {
            memberPremium.updateExpirationAt(memberPremium.getExpirationAt().plusMonths(12));
            return;
        }
        // 프리미엄 만료일이 지난 경우
        reactivateExpiredMemberPremium(memberPremium);
    }

    private void updateFreeToPremium(MemberPremium memberPremium) {
        memberPremium.updateIsFree(false);
        memberPremium.updateStartedAt(LocalDateTime.now());
        memberPremium.updateExpirationAt(LocalDateTime.now().plusMonths(12));
    }

    private void reactivateExpiredMemberPremium(MemberPremium memberPremium) {
        memberPremium.updateIsActive(true);
        memberPremium.updateStartedAt(LocalDateTime.now());
        memberPremium.updateExpirationAt(LocalDateTime.now().plusMonths(12));
    }

    private void createNewMemberPremium(Member member) {
        MemberPremium newMemberPremium = MemberPremium.builder()
            .member(member)
            .expirationAt(LocalDateTime.now().plusMonths(12))
            .isActive(true)
            .isFree(false)
            .build();
        memberPremiumRepository.save(newMemberPremium);
    }
}
