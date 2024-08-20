package com.keypoint.keypointtravel.campaign.service;

import com.keypoint.keypointtravel.blocked_member.repository.BlockedMemberRepository;
import com.keypoint.keypointtravel.campaign.dto.dto.EmailInvitationHistory;
import com.keypoint.keypointtravel.campaign.dto.dto.SendInvitationEmailDto;
import com.keypoint.keypointtravel.campaign.dto.useCase.InviteByEmailUseCase;
import com.keypoint.keypointtravel.campaign.dto.useCase.InviteFriendUseCase;
import com.keypoint.keypointtravel.campaign.entity.Campaign;
import com.keypoint.keypointtravel.campaign.entity.MemberCampaign;
import com.keypoint.keypointtravel.campaign.repository.EmailInvitationHistoryRepository;
import com.keypoint.keypointtravel.campaign.repository.CampaignRepository;
import com.keypoint.keypointtravel.campaign.repository.MemberCampaignRepository;
import com.keypoint.keypointtravel.global.enumType.email.EmailTemplate;
import com.keypoint.keypointtravel.global.enumType.error.CampaignErrorCode;
import com.keypoint.keypointtravel.global.enumType.error.MemberErrorCode;
import com.keypoint.keypointtravel.global.exception.GeneralException;
import com.keypoint.keypointtravel.global.utils.EmailUtils;
import com.keypoint.keypointtravel.member.dto.dto.CommonMemberDTO;
import com.keypoint.keypointtravel.member.entity.Member;
import com.keypoint.keypointtravel.member.repository.member.MemberRepository;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class InviteCampaignService {

    private final CampaignRepository campaignRepository;

    private final MemberCampaignRepository memberCampaignRepository;

    private final MemberRepository memberRepository;

    private final BlockedMemberRepository blockedMemberRepository;

    private final EmailInvitationHistoryRepository emailInvitationHistoryRepository;

    /**
     * 캠페인 이메일 초대 전 검증 함수
     *
     * @Param email, memberId(요청을 보낸 사용자), campaignId useCase
     */
    @Transactional
    public void validateInvitation(InviteByEmailUseCase useCase) {
        // 캠페인 장인지 확인 campaignId, memberId
        if (!campaignRepository.existsByCampaignLeaderTrue(useCase.getMemberId(),
            useCase.getCampaignId())) {
            throw new GeneralException(CampaignErrorCode.NOT_CAMPAIGN_OWNER);
        }
        // 이메일 존재 여부 확인
        CommonMemberDTO dto = memberRepository.findByEmail(useCase.getEmail())
            .orElseThrow(() -> new GeneralException(MemberErrorCode.NOT_EXISTED_EMAIL));
        // 현재 참여 인원들 중 차단 인원 여부 확인
        if (campaignRepository.existsBlockedMemberInCampaign(dto.getId(),
            useCase.getCampaignId())) {
            throw new GeneralException(CampaignErrorCode.BLOCKED_MEMBER_IN_CAMPAIGN);
        }
    }

    /**
     * 캠페인 이메일 초대 함수(이메일 전송, redis 이메일 저장)
     *
     * @Param email, memberId(요청을 보낸 사용자), campaignId useCase
     */
    @Async
    @Transactional
    public void sendEmail(InviteByEmailUseCase useCase) {
        // 캠페인 코드 및 로고 이미지를 포함한 이메일 전송
        // todo: 이메일 템플릿 - 초대하기 클릭 시 앱 연결 링크 추가
        SendInvitationEmailDto dto = campaignRepository.findSendInvitationEmailInfo(
            useCase.getCampaignId());
        Map<String, String> emailContent = new HashMap<>();
        emailContent.put("leaderName", dto.getLeaderName());
        emailContent.put("campaignName", dto.getCampaignName());
        emailContent.put("campaignCode", dto.getCampaignCode());
        List<String> images = new ArrayList<>();
        images.add("static/images/main-logo.jpg");
        EmailUtils.sendSingleEmailWithImages(
            useCase.getEmail(), EmailTemplate.INVITE_CAMPAIGN, emailContent, images);

        // 캠페인 이메일 초대 기록 Redis 에 저장(하루의 만료기간 설정)
        emailInvitationHistoryRepository.save(
            new EmailInvitationHistory(useCase.getMemberId(), useCase.getEmail(), 1L));
    }

    /**
     * 캠페인 친구 초대 함수
     *
     * @Param campaignId, memberId(요청을 보낸 사용자), friendIds(초대할 친구 id들) useCase
     */
    @Transactional
    public void inviteFriends(InviteFriendUseCase useCase) {
        // 캠페인 장인지 확인
        List<Long> memberIds = validateIsLeader(useCase);
        // 차단 여부 확인
        validateBlockedMember(memberIds, useCase.getFriendIds());
        // 이미 가입된 인원인지 확인
        validateJoinedMember(memberIds, useCase.getFriendIds());
        // 회원 - 캠페인 테이블에 추가
        saveMemberCampaigns(useCase);
        // todo : 대상자 및 캠페인 참여 인원들에게 알림 발송
    }

    private List<Long> validateIsLeader(InviteFriendUseCase useCase) {
        List<MemberCampaign> memberCampaigns = memberCampaignRepository.findAllByCampaignId(
            useCase.getCampaignId());
        // 요청한 member가 캠페인 장인지 확인, getId() 호출이기에 n + 1 문제 발생 가능성 없음.
        for (MemberCampaign memberCampaign : memberCampaigns) {
            if (memberCampaign.getMember().getId().equals(useCase.getMemberId())) {
                if (!memberCampaign.isLeader()) {
                    throw new GeneralException(CampaignErrorCode.NOT_CAMPAIGN_OWNER);
                }
                break;
            }
        }
        List<Long> memberIds = memberCampaigns.stream()
            .map(memberCampaign -> memberCampaign.getMember().getId())
            .toList();
        // 회원-캠페인에 memberId가 존재하지 않을 경우
        if (!memberIds.contains(useCase.getMemberId())) {
            throw new GeneralException(CampaignErrorCode.NOT_CAMPAIGN_OWNER);
        }
        return memberIds;
    }

    private void validateBlockedMember(List<Long> memberIds, List<Long> friendIds) {
        if (blockedMemberRepository.existsBlockedMembers(memberIds, friendIds)) {
            throw new GeneralException(CampaignErrorCode.BLOCKED_MEMBER_IN_CAMPAIGN);
        }
    }

    private void validateJoinedMember(List<Long> memberIds, List<Long> friendIds) {
        for (Long friendId : friendIds) {
            if (memberIds.contains(friendId)) {
                throw new GeneralException(CampaignErrorCode.DUPLICATED_MEMBER);
            }
        }
    }

    private void saveMemberCampaigns(InviteFriendUseCase useCase) {
        Campaign campaign = campaignRepository.getReferenceById(useCase.getCampaignId());
        List<MemberCampaign> memberCampaigns = new ArrayList<>();
        for (Long friendId : useCase.getFriendIds()) {
            Member member = memberRepository.getReferenceById(friendId);
            memberCampaigns.add(new MemberCampaign(campaign, member, false));
        }
        try {
            memberCampaignRepository.saveAll(memberCampaigns);
        } catch (Exception e) {
            throw new GeneralException(MemberErrorCode.NOT_EXISTED_MEMBER);
        }
    }
}
