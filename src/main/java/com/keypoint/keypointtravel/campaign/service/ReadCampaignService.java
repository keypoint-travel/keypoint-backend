package com.keypoint.keypointtravel.campaign.service;

import com.keypoint.keypointtravel.campaign.dto.dto.TotalBudgetDto;
import com.keypoint.keypointtravel.campaign.dto.useCase.FindPaymentUseCase;
import com.keypoint.keypointtravel.campaign.repository.CampaignBudgetRepository;
import com.keypoint.keypointtravel.campaign.repository.MemberCampaignRepository;
import com.keypoint.keypointtravel.global.enumType.error.CampaignErrorCode;
import com.keypoint.keypointtravel.global.exception.GeneralException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class ReadCampaignService {


    private final MemberCampaignRepository memberCampaignRepository;

    private final CampaignBudgetRepository campaignBudgetRepository;

    /**
     * 캠페인 결제 항목을 카테고리 별 조회하는 함수
     *
     * @Param campaignId, currencyType, memberId useCase
     *
     * @Return
     */
    @Transactional
    public void findByCategory(FindPaymentUseCase useCase) {
        // 0. 캠페인에 소속되어 있는지 검증
        if (!memberCampaignRepository.existsByCampaignIdAndMemberId(useCase.getCampaignId(), useCase.getMemberId())) {
            throw new GeneralException(CampaignErrorCode.NOT_EXISTED_CAMPAIGN);
        }
        // 1. 캠페인 아이디를 통해 총 에산 조회 TotalBudgetDto
        TotalBudgetDto totalBudget = campaignBudgetRepository.findTotalBudgetByCampaignId(useCase.getCampaignId());
        // 2. 캠페인 아이디를 통해 결제 항목 리스트 조회 (시간순)(영수증 아이디, 카테고리 이름, 결제일, 결제항목(식당 이름, , 금액), 화폐 단위)
    }
}
