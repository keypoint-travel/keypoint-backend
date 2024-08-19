package com.keypoint.keypointtravel.campaign.service;

import com.keypoint.keypointtravel.campaign.dto.dto.EmailInvitationHistory;
import com.keypoint.keypointtravel.campaign.dto.dto.SendInvitationEmailDto;
import com.keypoint.keypointtravel.campaign.dto.useCase.InviteByEmailUseCase;
import com.keypoint.keypointtravel.campaign.repository.EmailInvitationHistoryRepository;
import com.keypoint.keypointtravel.campaign.repository.CampaignRepository;
import com.keypoint.keypointtravel.global.enumType.email.EmailTemplate;
import com.keypoint.keypointtravel.global.enumType.error.CampaignErrorCode;
import com.keypoint.keypointtravel.global.enumType.error.MemberErrorCode;
import com.keypoint.keypointtravel.global.exception.GeneralException;
import com.keypoint.keypointtravel.global.utils.EmailUtils;
import com.keypoint.keypointtravel.member.dto.dto.CommonMemberDTO;
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


    private final MemberRepository memberRepository;

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
}
