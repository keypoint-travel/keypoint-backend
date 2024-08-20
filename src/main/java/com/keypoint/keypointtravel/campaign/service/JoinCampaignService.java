package com.keypoint.keypointtravel.campaign.service;

import com.keypoint.keypointtravel.campaign.dto.dto.EmailInvitationHistory;
import com.keypoint.keypointtravel.campaign.dto.useCase.JoinByEmailUseCase;
import com.keypoint.keypointtravel.campaign.entity.Campaign;
import com.keypoint.keypointtravel.campaign.entity.MemberCampaign;
import com.keypoint.keypointtravel.campaign.repository.CampaignRepository;
import com.keypoint.keypointtravel.campaign.repository.EmailInvitationHistoryRepository;
import com.keypoint.keypointtravel.campaign.repository.MemberCampaignRepository;
import com.keypoint.keypointtravel.friend.entity.Friend;
import com.keypoint.keypointtravel.friend.repository.FriendRepository;
import com.keypoint.keypointtravel.global.enumType.error.CampaignErrorCode;
import com.keypoint.keypointtravel.global.exception.GeneralException;
import com.keypoint.keypointtravel.member.entity.Member;
import com.keypoint.keypointtravel.member.repository.member.MemberRepository;

import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class JoinCampaignService {

    private final CampaignRepository campaignRepository;

    private final EmailInvitationHistoryRepository emailInvitationHistoryRepository;

    private final MemberCampaignRepository memberCampaignRepository;

    private final MemberRepository memberRepository;

    private final FriendRepository friendRepository;

    /**
     * 이메일을 통한 캠페인 가입 함수
     *
     * @Param email, memberId, campaignId useCase
     */
    @Transactional
    public void joinByEmail(JoinByEmailUseCase useCase) {
        // 이미 캠페인 가입이 된 상태일 시 예외 처리
        validateJoinedCampaign(useCase.getCampaignId(), useCase.getMemberId());
        // redis 캠페인 초대 이메일 목록에 저장되어 있는지(만료되지 않았는지 확인)
        List<EmailInvitationHistory> emailHistories = validateEmailHistoryInRedis(useCase);
        // 캠페인 리더 member 추출 & 진행 중인 캠페인인지 확인
        MemberCampaign leader = campaignRepository.findCampaignLeader(useCase.getCampaignId());
        // 회원 - 캠페인 태이블 추가(가입)
        Member member = saveMemberCampaign(useCase);
        // 캠페인 리더와 친구관계 구축
        saveFriends(leader, member);
        // redis 에서 해당 이메일 초대 이력 삭제(중복 포함)
        emailInvitationHistoryRepository.deleteAll(emailHistories);
        // todo : 대상자 및 캠페인 참여 인원들에게 알림 발송
    }

    private void validateJoinedCampaign(Long campaignId, Long memberId) {
        if (memberCampaignRepository.existsByCampaignIdAndMemberId(campaignId, memberId)) {
            throw new GeneralException(CampaignErrorCode.DUPLICATED_MEMBER);
        }
    }

    private List<EmailInvitationHistory> validateEmailHistoryInRedis(JoinByEmailUseCase useCase) {
        List<EmailInvitationHistory> histories =
            emailInvitationHistoryRepository.findByCampaignId(useCase.getCampaignId());
        // 중복 포함 동일한 email 가진 기록 조회
        List<EmailInvitationHistory> emailHistories = histories.stream()
            .filter(history -> history.getEmail().equals(useCase.getEmail()))
            .toList();
        // 기간 만료 시 예외 처리
        if (emailHistories.isEmpty()) {
            throw new GeneralException(CampaignErrorCode.EXPIRED_INVITE_EMAIL);
        }
        return histories;
    }

    private Member saveMemberCampaign(JoinByEmailUseCase useCase) {
        Member member = memberRepository.getReferenceById(useCase.getMemberId());
        Campaign campaign = campaignRepository.getReferenceById(useCase.getCampaignId());
        memberCampaignRepository.save(new MemberCampaign(campaign, member, false));
        return member;
    }

    private void saveFriends(MemberCampaign leader, Member member) {
        if (!friendRepository.existsByFriendIdAndMemberIdAndIsDeletedFalse(leader.getMember().getId(), member.getId())) {
            friendRepository.save(buildFriend(leader.getMember(), member));
            friendRepository.save(buildFriend(member, leader.getMember()));
        }
    }

    private Friend buildFriend(Member findedMember, Member member) {
        return Friend.builder()
            .friendId(findedMember.getId())
            .member(member)
            .isDeleted(false)
            .build();
    }
}
