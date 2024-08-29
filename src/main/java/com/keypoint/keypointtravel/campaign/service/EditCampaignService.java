package com.keypoint.keypointtravel.campaign.service;

import com.keypoint.keypointtravel.campaign.dto.dto.CampaignInfoDto;
import com.keypoint.keypointtravel.campaign.dto.dto.MemberInfoDto;
import com.keypoint.keypointtravel.campaign.dto.dto.TravelLocationDto;
import com.keypoint.keypointtravel.campaign.dto.response.EditCampaignResponse;
import com.keypoint.keypointtravel.campaign.dto.useCase.FIndCampaignUseCase;
import com.keypoint.keypointtravel.campaign.entity.CampaignBudget;
import com.keypoint.keypointtravel.campaign.repository.CampaignBudgetRepository;
import com.keypoint.keypointtravel.campaign.repository.CampaignRepository;
import com.keypoint.keypointtravel.global.enumType.error.CampaignErrorCode;
import com.keypoint.keypointtravel.global.exception.GeneralException;
import com.keypoint.keypointtravel.member.repository.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EditCampaignService {

    private final CampaignBudgetRepository campaignBudgetRepository;

    private final MemberRepository memberRepository;

    private final CampaignRepository campaignRepository;

    @Transactional
    public EditCampaignResponse findCampaign(FIndCampaignUseCase useCase) {
        // 캠페인 장인지 확인 필요
        if (!campaignRepository.existsByCampaignLeaderTrue(useCase.getMemberId(),
            useCase.getCampaignId())) {
            throw new GeneralException(CampaignErrorCode.NOT_CAMPAIGN_OWNER);
        }
        // 캠페인 id, 이미지 url, 제목, 시작일, 종료일 조회
        CampaignInfoDto campaign = campaignRepository.findCampaignInfo(useCase.getCampaignId());
        // 여행지 리스트 조회
        // todo : 조회 시 다국어 설정 필요
        List<TravelLocationDto> travelLocations = campaignRepository.findTravelLocationList(useCase.getCampaignId());
        // 예산 리스트 조회
        List<CampaignBudget> campaignBudgets = campaignBudgetRepository.findAllByCampaignId(useCase.getCampaignId());
        // 참여인원 리스트 조회
        List<MemberInfoDto> dtoList = memberRepository.findCampaignMemberList(useCase.getCampaignId());
        return new EditCampaignResponse(campaign, travelLocations, campaignBudgets, dtoList);
    }
}
