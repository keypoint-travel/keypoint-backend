package com.keypoint.keypointtravel.campaign.service;

import com.keypoint.keypointtravel.blocked_member.repository.BlockedMemberRepository;
import com.keypoint.keypointtravel.campaign.dto.response.FindInvitationResponse;
import com.keypoint.keypointtravel.campaign.dto.useCase.FIndCampaignUseCase;
import com.keypoint.keypointtravel.campaign.entity.EmailInvitationHistory;
import com.keypoint.keypointtravel.campaign.dto.dto.SendInvitationEmailDto;
import com.keypoint.keypointtravel.campaign.dto.useCase.CampaignEmailUseCase;
import com.keypoint.keypointtravel.campaign.dto.useCase.InviteFriendUseCase;
import com.keypoint.keypointtravel.campaign.entity.Campaign;
import com.keypoint.keypointtravel.campaign.entity.InvitationProhibitionHistory;
import com.keypoint.keypointtravel.campaign.entity.MemberCampaign;
import com.keypoint.keypointtravel.campaign.repository.CustomMemberCampaignRepository;
import com.keypoint.keypointtravel.campaign.repository.EmailInvitationHistoryRepository;
import com.keypoint.keypointtravel.campaign.repository.CampaignRepository;
import com.keypoint.keypointtravel.campaign.repository.InvitationProhibitionHistoryRepository;
import com.keypoint.keypointtravel.campaign.repository.MemberCampaignRepository;
import com.keypoint.keypointtravel.friend.dto.FriendDto;
import com.keypoint.keypointtravel.friend.repository.FriendRepository;
import com.keypoint.keypointtravel.global.enumType.email.EmailTemplate;
import com.keypoint.keypointtravel.global.enumType.error.CampaignErrorCode;
import com.keypoint.keypointtravel.global.enumType.error.MemberErrorCode;
import com.keypoint.keypointtravel.global.exception.GeneralException;
import com.keypoint.keypointtravel.global.utils.EmailUtils;
import com.keypoint.keypointtravel.member.entity.Member;
import com.keypoint.keypointtravel.member.repository.member.MemberRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.context.i18n.LocaleContextHolder;
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

    private final InvitationProhibitionHistoryRepository invitationProhibitionHistoryRepository;

    private final CustomMemberCampaignRepository customMemberCampaignRepository;

    private final FriendRepository friendRepository;

    /**
     * 캠페인 초대 화면 조회 함수
     *
     * @Param memberId, campaignId useCase
     * @Return 캠페인 정보 및 친구 목록
     */
    @Transactional
    public FindInvitationResponse findInvitationView(FIndCampaignUseCase useCase) {
        // 캠페인 장인인지 확인
        if (!customMemberCampaignRepository.existsByCampaignLeaderTrue(useCase.getMemberId(),
            useCase.getCampaignId())) {
            throw new GeneralException(CampaignErrorCode.NOT_CAMPAIGN_OWNER);
        }
        // 캠페인 정보 조회
        Campaign campaign = campaignRepository.findById(useCase.getCampaignId())
            .orElseThrow(() -> new GeneralException(CampaignErrorCode.NOT_EXISTED_CAMPAIGN));
        // 친구 목록 조회
        List<FriendDto> friends = friendRepository.findAllByMemberId(useCase.getMemberId());
        return new FindInvitationResponse(campaign.getId(), campaign.getTitle(),
            campaign.getInvitation_code(), friends);
    }

    /**
     * 캠페인 이메일 초대 전 검증 함수
     *
     * @Param email, memberId(요청을 보낸 사용자), campaignId useCase
     */
    public void validateInvitation(CampaignEmailUseCase useCase) {
        // 이메일 초대 금지 상태 확인
        if (invitationProhibitionHistoryRepository.existsByCampaignId(useCase.getCampaignId())) {
            throw new GeneralException(CampaignErrorCode.PROHIBIT_INVITE_EMAIL);
        }
        // 캠페인 장인지 확인 campaignId, memberId
        if (!customMemberCampaignRepository.existsByCampaignLeaderTrue(useCase.getMemberId(),
            useCase.getCampaignId())) {
            throw new GeneralException(CampaignErrorCode.NOT_CAMPAIGN_OWNER);
        }
        //  email 초대 기록 조회
        Optional<EmailInvitationHistory> emailInvitationHistory =
            emailInvitationHistoryRepository.findByCampaignIdAndEmail(useCase.getCampaignId(),
                useCase.getEmail());
        // 재전송이 아닐 경우 - 이미 초대한 메일 기록이 있는지 확인
        if (!useCase.isResend()) {
            // 이미 초대한 이력이 있을 경우
            if (emailInvitationHistory.isPresent()) {
                throw new GeneralException(CampaignErrorCode.ALREADY_INVITE_EMAIL);
            }
        }
        // 재전송일 경우 - 3회 이상 초대하였을 경우 예외 처리 및 초대 금지 기한 설정
        if (useCase.isResend() && emailInvitationHistory.isPresent()
            && emailInvitationHistory.get().getCount() >= 3) {
            // 1일동안 초대 금지 부여
            InvitationProhibitionHistory history = new InvitationProhibitionHistory(
                useCase.getCampaignId(), 1L);
            invitationProhibitionHistoryRepository.save(history);
            throw new GeneralException(CampaignErrorCode.PROHIBIT_INVITE_EMAIL);
        }
    }

    /**
     * 캠페인 이메일 초대 함수(이메일 전송, redis 이메일 저장)
     *
     * @Param email, memberId(요청을 보낸 사용자), campaignId useCase
     */
    @Async
    @Transactional
    public void sendEmail(CampaignEmailUseCase useCase, Locale locale) {
        LocaleContextHolder.setLocale(locale);
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
        EmailUtils.sendSingleEmailWithImages(useCase.getEmail(), EmailTemplate.INVITE_CAMPAIGN,
            new Object[]{dto.getCampaignName()}, emailContent, images);

        // 캠페인 이메일 초대 기록 Redis 에 저장(하루의 만료기간 설정)
        EmailInvitationHistory history = emailInvitationHistoryRepository.findByCampaignIdAndEmail(
            useCase.getCampaignId(), useCase.getEmail()).orElse(
            new EmailInvitationHistory(useCase.getCampaignId(), useCase.getEmail(), 0));
        history.addCount();
        emailInvitationHistoryRepository.save(history);
    }

    /**
     * 캠페인 친구 초대 함수
     *
     * @Param campaignId, memberId(요청을 보낸 사용자), friendIds(초대할 친구 id들) useCase
     */
    @Transactional
    public void inviteFriends(InviteFriendUseCase useCase) {
        // 1. 캠페인 장인지 확인
        List<Long> memberIds = validateIsLeader(useCase);
        // 2. 차단 여부 확인
        validateBlockedMember(memberIds, useCase.getFriendIds());
        // 3. 이미 가입된 인원인지 확인
        validateJoinedMember(memberIds, useCase.getFriendIds());
        // 4. 참여한 캠페인 수 및 프리미엄 회원인지 검증
        validatePremiumMember(useCase);
        // 5. 회원 - 캠페인 테이블에 추가
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

    private void validatePremiumMember(InviteFriendUseCase useCase) {
        // 가입한 캠페인 수가 1개 이상이지만 프리미엄 회원이 아닌지 검증
        if (customMemberCampaignRepository.existsMultipleCampaignNotPremium(
            useCase.getFriendIds())) {
            throw new GeneralException(CampaignErrorCode.MULTIPLE_CAMPAIGN_NON_PREMIUM);
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
