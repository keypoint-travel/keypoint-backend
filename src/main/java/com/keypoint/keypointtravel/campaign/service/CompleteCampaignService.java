package com.keypoint.keypointtravel.campaign.service;

import com.keypoint.keypointtravel.campaign.dto.useCase.CompleteCampaignUseCase;
import com.keypoint.keypointtravel.campaign.entity.MemberCampaign;
import com.keypoint.keypointtravel.campaign.repository.CampaignRepository;
import com.keypoint.keypointtravel.campaign.repository.MemberCampaignRepository;
import com.keypoint.keypointtravel.global.enumType.error.CampaignErrorCode;
import com.keypoint.keypointtravel.global.exception.GeneralException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CompleteCampaignService {

    private final MemberCampaignRepository memberCampaignRepository;

    private final CampaignRepository campaignRepository;

    /**
     * 캠페인 종료 함수
     *
     * @Param campaignId, memberId useCase
     */
    @Transactional
    public void completeCampaign(CompleteCampaignUseCase useCase) {
        // 1. 캠페인 장인지 확인(알림 발송을 위해 회원 리스트 조회)
        List<Long> memberIds = validateIsLeader(useCase.getMemberId(), useCase.getCampaignId());
        // 2. 캠페인 종료 (status 변경)
        campaignRepository.updateCampaignFinished(useCase.getCampaignId());
        // 3. todo : 캠페인 종료 알림 전송
    }

    private List<Long> validateIsLeader(Long memberId, Long campaignId) {
        List<MemberCampaign> memberCampaigns = memberCampaignRepository.findAllByCampaignId(campaignId);
        // 요청한 member가 캠페인 장인지 확인, getId() 호출이기에 n + 1 문제 발생 가능성 없음.
        for (MemberCampaign memberCampaign : memberCampaigns) {
            if (memberCampaign.getMember().getId().equals(memberId)) {
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
        if (!memberIds.contains(memberId)) {
            throw new GeneralException(CampaignErrorCode.NOT_CAMPAIGN_OWNER);
        }
        return memberIds;
    }
}
