package com.keypoint.keypointtravel.campaign.service;

import com.keypoint.keypointtravel.campaign.dto.dto.CampaignInfoDto;
import com.keypoint.keypointtravel.campaign.dto.dto.MemberInfoDto;
import com.keypoint.keypointtravel.campaign.dto.dto.ReceiptInfoDto;
import com.keypoint.keypointtravel.campaign.dto.response.details.CampaignDetailsResponse;
import com.keypoint.keypointtravel.campaign.dto.response.details.ReceiptInfo;
import com.keypoint.keypointtravel.campaign.dto.useCase.FIndCampaignUseCase;
import com.keypoint.keypointtravel.campaign.repository.CampaignRepository;
import com.keypoint.keypointtravel.campaign.repository.MemberCampaignRepository;
import com.keypoint.keypointtravel.global.enumType.error.CampaignErrorCode;
import com.keypoint.keypointtravel.global.exception.GeneralException;
import com.keypoint.keypointtravel.member.repository.member.MemberRepository;
import com.keypoint.keypointtravel.receipt.repository.CustomPaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FindCampaignService {

    private final CampaignRepository campaignRepository;

    private final CustomPaymentRepository customPaymentRepository;

    private final MemberRepository memberRepository;

    private final MemberCampaignRepository memberCampaignRepository;

    /**
     * 캠페인 상세 페이지 상단 부분 조회 함수
     *
     * @Param campaignId, memberId useCase
     * @Return CampaignDetailsResponse
     */
    @Transactional(readOnly = true)
    public CampaignDetailsResponse findCampaignDetails(FIndCampaignUseCase useCase) {
        // 0. 캠페인에 소속되어 있는지 검증
        validateInCampaign(useCase);
        // 1. 캠페인 정보 조회
        CampaignInfoDto campaign = campaignRepository.findCampaignInfo(useCase.getCampaignId());
        // 2. 캠페인 아이디에 해당하는 회원 정보 리스트 조회
        List<MemberInfoDto> dtoList = memberRepository.findCampaignMemberList(useCase.getCampaignId());
        // 3. 캠페인 아이디에 해당하는 List 기간 순 정렬[영수증 아이디, 이미지, 기간, 위도, 경도]
        List<ReceiptInfoDto> receiptInfoDtoList = customPaymentRepository.findAllOrderByPaidAt(useCase.getCampaignId());
        List<ReceiptInfo> receiptList = new ArrayList<>();
        for (int i = 0; i < receiptInfoDtoList.size(); i++) {
            receiptList.add(new ReceiptInfo(
                receiptInfoDtoList.get(i).getReceiptId(),
                receiptInfoDtoList.get(i).getReceiptImage(),
                i + 1,
                receiptInfoDtoList.get(i).getLatitude(),
                receiptInfoDtoList.get(i).getLongitude()));
        }
        // 4. 응답
        return CampaignDetailsResponse.of(campaign, dtoList, receiptList);
    }

    private void validateInCampaign(FIndCampaignUseCase useCase) {
        if (!memberCampaignRepository.existsByCampaignIdAndMemberId(useCase.getCampaignId(), useCase.getMemberId())) {
            throw new GeneralException(CampaignErrorCode.NOT_EXISTED_CAMPAIGN);
        }
    }

    @Transactional(readOnly = true)
    public List<CampaignInfoDto> findCampaigns(Long memberId){
        // 1. 캠페인 정보 조회
        return campaignRepository.findCampaignInfoList(memberId);
    }
}
