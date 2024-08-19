package com.keypoint.keypointtravel.campaign.service;

import com.keypoint.keypointtravel.campaign.dto.useCase.InviteByEmailUseCase;
import com.keypoint.keypointtravel.campaign.repository.CampaignRepository;
import com.keypoint.keypointtravel.global.enumType.error.CampaignErrorCode;
import com.keypoint.keypointtravel.global.enumType.error.MemberErrorCode;
import com.keypoint.keypointtravel.global.exception.GeneralException;
import com.keypoint.keypointtravel.member.dto.dto.CommonMemberDTO;
import com.keypoint.keypointtravel.member.repository.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class InviteCampaignService {

    private final CampaignRepository campaignRepository;


    private final MemberRepository memberRepository;


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
}
