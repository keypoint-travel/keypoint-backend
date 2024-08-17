package com.keypoint.keypointtravel.campaign.service;

import com.keypoint.keypointtravel.campaign.dto.request.createRequest.MemberInfo;
import com.keypoint.keypointtravel.campaign.dto.useCase.CreateUseCase;
import com.keypoint.keypointtravel.campaign.entity.Campaign;
import com.keypoint.keypointtravel.campaign.entity.CampaignBudget;
import com.keypoint.keypointtravel.campaign.entity.MemberCampaign;
import com.keypoint.keypointtravel.campaign.entity.TravelLocation;
import com.keypoint.keypointtravel.campaign.repository.CampaignBudgetRepository;
import com.keypoint.keypointtravel.campaign.repository.CampaignRepository;
import com.keypoint.keypointtravel.campaign.repository.MemberCampaignRepository;
import com.keypoint.keypointtravel.campaign.repository.TravelLocationRepository;
import com.keypoint.keypointtravel.global.constants.DirectoryConstants;
import com.keypoint.keypointtravel.global.enumType.campaign.Status;
import com.keypoint.keypointtravel.global.enumType.error.MemberErrorCode;
import com.keypoint.keypointtravel.global.exception.GeneralException;
import com.keypoint.keypointtravel.global.utils.StringUtils;
import com.keypoint.keypointtravel.member.entity.Member;
import com.keypoint.keypointtravel.member.repository.member.MemberRepository;
import com.keypoint.keypointtravel.uploadFile.service.UploadFileService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CreateCampaignService {

    private final CampaignRepository campaignRepository;

    private final CampaignBudgetRepository campaignBudgetRepository;

    private final MemberCampaignRepository memberCampaignRepository;

    private final MemberRepository memberRepository;

    private final TravelLocationRepository travelLocationRepository;

    private final UploadFileService uploadFileService;

    /**
     * 캠페인 생성 함수
     *
     * @Param 커버 이미지, 캠페인 정보, 회원 정보, 예산 정보 등 캠페인 생성에 필요한 정보 useCase
     */
    @Transactional
    public void createCampaign(CreateUseCase useCase) {
        // 1. 커버 사진 upload File 저장
        Long coverImageId = saveUploadFile(useCase.getCoverImage());
        // 2. 켐페인 저장
        Campaign campaign = saveCampaign(useCase, coverImageId);
        // 3. 켐페인 예산 저장
        saveCampaignBudgets(campaign, useCase);
        // 4. 회원 캠페인 저장
        saveMemberCampaigns(campaign, useCase);
        // 5. 여행지 저장
        saveTravelLocations(campaign, useCase);
    }

    private Long saveUploadFile(MultipartFile coverImage) {
        try {
            return uploadFileService.saveUploadFile(coverImage,
                DirectoryConstants.CAMPAIGN_COVER_DIRECTORY
            );
        } catch (IOException e) {
            throw new GeneralException(e);
        }
    }

    private Campaign saveCampaign(CreateUseCase useCase, Long coverImageId) {
        Campaign campaign = Campaign.builder()
            .title(useCase.getTitle())
            .status(Status.IN_PROGRESS)
            .campaignImageId(coverImageId)
            .startDate(useCase.getStartDate())
            .endDate(useCase.getEndDate())
            .build();
        campaignRepository.save(campaign);
        campaign.addInvitationCode(StringUtils.getRandomString(9) + campaign.getId());
        return campaign;
    }

    private void saveCampaignBudgets(Campaign campaign, CreateUseCase useCase) {
        List<CampaignBudget> campaignBudgets = useCase.getBudgets().stream()
            .map(budget -> CampaignBudget.builder()
                .campaign(campaign)
                .category(budget.getCategory())
                .amount(budget.getAmount())
                .currency(budget.getCurrency())
                .build()).toList();
        campaignBudgetRepository.saveAll(campaignBudgets);
    }

    private void saveMemberCampaigns(Campaign campaign, CreateUseCase useCase) {
        List<MemberCampaign> memberCampaigns = new ArrayList<>();
        // 함께 참여하는 회원들 저장
        for (MemberInfo memberInfo : useCase.getMembers()) {
            if (memberInfo.getMemberId().equals(useCase.getMemberId())) {
                continue;
            }
            Member member = memberRepository.getReferenceById(memberInfo.getMemberId());
            memberCampaigns.add(new MemberCampaign(campaign, member, false));
        }
        // 캠페인을 생성하는 회원(캠페인 장) 저장
        Member member = memberRepository.getReferenceById(useCase.getMemberId());
        memberCampaigns.add(new MemberCampaign(campaign, member, true));
        try {
            memberCampaignRepository.saveAll(memberCampaigns);
        } catch (Exception e) {
            throw new GeneralException(MemberErrorCode.NOT_EXISTED_MEMBER);
        }
    }

    private void saveTravelLocations(Campaign campaign, CreateUseCase useCase) {
        List<TravelLocation> travelLocations = useCase.getTravels().stream()
            .map(travel -> new TravelLocation(campaign, travel.getPlaceId(), travel.getSequence()))
            .toList();
        travelLocationRepository.saveAll(travelLocations);
    }
}
