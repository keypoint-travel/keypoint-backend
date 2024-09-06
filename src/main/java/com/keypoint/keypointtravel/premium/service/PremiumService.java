package com.keypoint.keypointtravel.premium.service;

import com.keypoint.keypointtravel.member.entity.Member;
import com.keypoint.keypointtravel.member.repository.member.MemberRepository;
import com.keypoint.keypointtravel.premium.entity.MemberPremium;
import com.keypoint.keypointtravel.premium.repository.MemberPremiumRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PremiumService {

    private final MemberRepository memberRepository;
    private final MemberPremiumRepository memberPremiumRepository;

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
