package com.keypoint.keypointtravel.campaign.service;

import com.keypoint.keypointtravel.blocked_member.repository.BlockedMemberRepository;
import com.keypoint.keypointtravel.campaign.dto.dto.MemberInfoDto;
import com.keypoint.keypointtravel.campaign.dto.response.CampaignWaitMemberResponse;
import com.keypoint.keypointtravel.campaign.dto.useCase.*;
import com.keypoint.keypointtravel.campaign.entity.CampaignWaitMember;
import com.keypoint.keypointtravel.campaign.entity.EmailInvitationHistory;
import com.keypoint.keypointtravel.campaign.entity.Campaign;
import com.keypoint.keypointtravel.campaign.entity.MemberCampaign;
import com.keypoint.keypointtravel.campaign.repository.*;
import com.keypoint.keypointtravel.friend.entity.Friend;
import com.keypoint.keypointtravel.friend.repository.FriendRepository;
import com.keypoint.keypointtravel.global.enumType.error.CampaignErrorCode;
import com.keypoint.keypointtravel.global.enumType.error.MemberErrorCode;
import com.keypoint.keypointtravel.global.exception.GeneralException;
import com.keypoint.keypointtravel.member.entity.Member;
import com.keypoint.keypointtravel.member.repository.member.MemberRepository;

import java.util.List;

import java.util.Optional;
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

    private final BlockedMemberRepository blockedMemberRepository;

    private final CampaignWaitMemberRepository campaignWaitMemberRepository;

    private final CustomMemberCampaignRepository customMemberCampaignRepository;

    /**
     * 이메일을 통한 캠페인 가입 함수
     *
     * @Param email, memberId, campaignId useCase
     */
    @Transactional
    public void joinByEmail(JoinByEmailUseCase useCase) {
        // 1. redis 캠페인 초대 이메일 목록에 저장되어 있는지(만료되지 않았는지 확인)
        EmailInvitationHistory emailHistory = validateEmailHistoryInRedis(useCase);
        // 2. 이미 캠페인 가입이 된 상태일 시 예외 처리
        validateJoinedCampaign(useCase.getCampaignId(), useCase.getMemberId());
        // 3. 현재 참여 인원들 중 차단 인원 여부 확인
        if (customMemberCampaignRepository.existsBlockedMemberInCampaign(useCase.getMemberId(),
            useCase.getCampaignId())) {
            throw new GeneralException(CampaignErrorCode.BLOCKED_MEMBER_IN_CAMPAIGN);
        }
        // 4. 캠페인 리더 member 추출 & 진행 중인 캠페인인지 확인
        MemberCampaign leader = customMemberCampaignRepository.findCampaignLeader(
            useCase.getCampaignId());
        // 5. 참여한 캠페인 수 및 프리미엄 회원인지 검증
        validatePremiumMember(useCase.getMemberId());
        // 6. 회원 - 캠페인 태이블 추가(가입)
        Member member = saveMemberCampaign(useCase.getMemberId(), useCase.getCampaignId());
        // 7. 캠페인 리더와 친구관계 구축
        saveFriends(leader.getMember(), member);
        // 8. redis 에서 해당 이메일 초대 이력 삭제(중복 포함)
        emailInvitationHistoryRepository.delete(emailHistory);
        // todo : 대상자 및 캠페인 참여 인원들에게 알림 발송
    }

    private void validateJoinedCampaign(Long campaignId, Long memberId) {
        if (memberCampaignRepository.existsByCampaignIdAndMemberId(campaignId, memberId)) {
            throw new GeneralException(CampaignErrorCode.DUPLICATED_MEMBER);
        }
    }

    private EmailInvitationHistory validateEmailHistoryInRedis(JoinByEmailUseCase useCase) {
        //  email 초대 기록 조회
        Optional<EmailInvitationHistory> history =
            emailInvitationHistoryRepository.findByCampaignIdAndEmail(useCase.getCampaignId(),
                useCase.getEmail());
        // 기간 만료 시 예외 처리
        if (history.isEmpty()) {
            throw new GeneralException(CampaignErrorCode.EXPIRED_INVITE_EMAIL);
        }
        return history.get();
    }

    private Friend buildFriend(Member findedMember, Member member) {
        return Friend.builder()
            .friendId(findedMember.getId())
            .member(member)
            .isDeleted(false)
            .build();
    }

    /**
     * 캠페인 코드를 통한 가입 신청 함수
     *
     * @Param memberId, campaignCode useCase
     */
    @Transactional
    public void requestJoinByCampaignCode(JoinByCodeUseCase useCase) {
        // 1. 캠페인 코드에 해당하는 캠페인이 존재하지 않을 시 예외 처리
        List<MemberCampaign> memberCampaigns = customMemberCampaignRepository
            .findMembersByCampaignCode(useCase.getCampaignCode());
        if (memberCampaigns.isEmpty()) {
            throw new GeneralException(CampaignErrorCode.NOT_EXISTED_CAMPAIGN);
        }
        // 2. 이미 신청을 하였을 경우 예외 처리
        if (campaignWaitMemberRepository.existsByCampaignIdAndMemberId(
            memberCampaigns.get(0).getCampaign().getId(), useCase.getMemberId())) {
            throw new GeneralException(CampaignErrorCode.ALREADY_IN_WAIT_LIST);
        }
        List<Long> memberIds = memberCampaigns.stream()
            .map(memberCampaign -> memberCampaign.getMember().getId())
            .toList();
        // 3. 이미 캠페인 가입이 된 상태일 시 예외 처리
        for (Long memberId : memberIds) {
            if (memberId.equals(useCase.getMemberId())) {
                throw new GeneralException(CampaignErrorCode.DUPLICATED_MEMBER);
            }
        }
        // 4. 캠페인 인원들 중 차단 여부 확인
        if (blockedMemberRepository.existsBlockedMembers(memberIds, useCase.getMemberId())) {
            throw new GeneralException(CampaignErrorCode.BLOCKED_MEMBER_IN_CAMPAIGN);
        }
        // 5. 참여한 캠페인 수 및 프리미엄 회원인지 검증
        validatePremiumMember(useCase.getMemberId());
        // 6. 캠페인 대기 회원에 추가
        Member member = memberRepository.getReferenceById(useCase.getMemberId());
        campaignWaitMemberRepository.save(
            new CampaignWaitMember(memberCampaigns.get(0).getCampaign(), member));
        // todo : 켐페인 장을 대상으로 알림 발송 (참여 승인 수락, 거절 알림)
    }

    /**
     * 캠페인 가입 신청을 승인, 거절하는 함수
     *
     * @Param leaderId, isApprove, memberId, campaignId useCase
     */
    @Transactional
    public void approveJoinByCampaignCode(ApproveByCodeUseCase useCase) {
        // 1. 캠페인 id에 해당하는 캠페인이 존재하는지, 캠페인 장인지 확인
        List<Long> memberIds = validateIsLeader(useCase);
        // 2. 캠페인 가입 신청 대기 목록에 존재하는지 확인 후 삭제
        customMemberCampaignRepository.deleteWaitMember(useCase.getMemberId(),
            useCase.getCampaignId());
        if (useCase.isApprove()) {
            // 캠페인 참여 승인하였을 경우
            // 3. 이미 캠페인 가입이 된 상태일 시 예외 처리
            if (memberIds.contains(useCase.getMemberId())) {
                throw new GeneralException(CampaignErrorCode.DUPLICATED_MEMBER);
            }
            // 4. 참여한 캠페인 수 및 프리미엄 회원인지 검증
            validatePremiumMember(useCase.getMemberId());
            // 5. 회원 - 캠페인 태이블 추가(가입)
            Member member = saveMemberCampaign(useCase.getMemberId(), useCase.getCampaignId());
            // 6. 캠페인 리더와 친구관계 구축
            Member leader = memberRepository.getReferenceById(useCase.getLeaderId());
            saveFriends(leader, member);
            // todo : 대상자 및 캠페인 참여 인원들에게 새 인원 참여 알림 발송
        } else {
            // 3. 캠페인 참여 거절하였을 경우
            // todo : 대상자에게 거절 알림 발송
        }
    }

    private List<Long> validateIsLeader(ApproveByCodeUseCase useCase) {
        List<MemberCampaign> memberCampaigns = customMemberCampaignRepository
            .findMembersByCampaignCode(useCase.getCampaignId());
        // 캠페인 id에 해당하는 캠페인이 존재하지 않을 시 예외 처리
        if (memberCampaigns.isEmpty()) {
            throw new GeneralException(CampaignErrorCode.NOT_EXISTED_CAMPAIGN);
        }
        // 요청한 사용자가 캠페인 장인지 확인, getId() 호출이기에 n + 1 문제 발생 가능성 없음.
        for (MemberCampaign memberCampaign : memberCampaigns) {
            if (memberCampaign.getMember().getId().equals(useCase.getLeaderId())) {
                if (!memberCampaign.isLeader()) {
                    throw new GeneralException(CampaignErrorCode.NOT_CAMPAIGN_OWNER);
                }
                break;
            }
        }
        List<Long> memberIds = memberCampaigns.stream()
            .map(memberCampaign -> memberCampaign.getMember().getId())
            .toList();
        // 회원-캠페인에 leaderId가 존재하지 않을 경우
        if (!memberIds.contains(useCase.getLeaderId())) {
            throw new GeneralException(CampaignErrorCode.NOT_CAMPAIGN_OWNER);
        }
        return memberIds;
    }

    private Member saveMemberCampaign(Long memberId, Long campaignId) {
        try {
            Member member = memberRepository.getReferenceById(memberId);
            Campaign campaign = campaignRepository.getReferenceById(campaignId);
            memberCampaignRepository.save(new MemberCampaign(campaign, member, false));
            return member;
        } catch (Exception e) {
            throw new GeneralException(MemberErrorCode.NOT_EXISTED_MEMBER);
        }
    }

    private void saveFriends(Member leader, Member member) {
        if (!friendRepository.existsByFriendIdAndMemberIdAndIsDeletedFalse(leader.getId(),
            member.getId())) {
            friendRepository.save(buildFriend(leader, member));
            friendRepository.save(buildFriend(member, leader));
        }
    }

    /**
     * 캠페인 참여 대기 인원 목록 조회 함수
     *
     * @Param memberId, campaignId useCase
     * @Return
     */
    @Transactional(readOnly = true)
    public CampaignWaitMemberResponse findCampaignWaitList(FIndCampaignUseCase useCase) {
        // 캠페인 장인지 확인 필요
        if (!customMemberCampaignRepository.existsByCampaignLeaderTrue(useCase.getMemberId(),
            useCase.getCampaignId())) {
            throw new GeneralException(CampaignErrorCode.NOT_CAMPAIGN_OWNER);
        }
        List<MemberInfoDto> waitMembers = customMemberCampaignRepository.findWaitMembers(
            useCase.getCampaignId());
        return new CampaignWaitMemberResponse(useCase.getCampaignId(), waitMembers);
    }

    private void validatePremiumMember(Long memberId) {
        List<Long> memberIds = List.of(memberId);
        // 가입한 캠페인 수가 1개 이상이지만 프리미엄 회원이 아닌지 검증
        if (customMemberCampaignRepository.existsMultipleCampaignNotPremium(memberIds)) {
            throw new GeneralException(CampaignErrorCode.MULTIPLE_CAMPAIGN_NON_PREMIUM);
        }
    }
}
