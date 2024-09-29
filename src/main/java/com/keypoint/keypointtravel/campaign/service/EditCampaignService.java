package com.keypoint.keypointtravel.campaign.service;

import com.keypoint.keypointtravel.blocked_member.repository.BlockedMemberRepository;
import com.keypoint.keypointtravel.campaign.dto.dto.CampaignInfoDto;
import com.keypoint.keypointtravel.campaign.dto.dto.MemberInfoDto;
import com.keypoint.keypointtravel.campaign.dto.dto.TravelLocationDto;
import com.keypoint.keypointtravel.campaign.dto.request.MemberInfo;
import com.keypoint.keypointtravel.campaign.dto.request.createRequest.BudgetInfo;
import com.keypoint.keypointtravel.campaign.dto.request.createRequest.TravelInfo;
import com.keypoint.keypointtravel.campaign.dto.response.EditCampaignResponse;
import com.keypoint.keypointtravel.campaign.dto.useCase.CreateUseCase;
import com.keypoint.keypointtravel.campaign.dto.useCase.UpdateUseCase;
import com.keypoint.keypointtravel.campaign.dto.useCase.FIndCampaignUseCase;
import com.keypoint.keypointtravel.campaign.entity.Campaign;
import com.keypoint.keypointtravel.campaign.entity.CampaignBudget;
import com.keypoint.keypointtravel.campaign.entity.MemberCampaign;
import com.keypoint.keypointtravel.campaign.entity.TravelLocation;
import com.keypoint.keypointtravel.campaign.repository.*;
import com.keypoint.keypointtravel.global.constants.DirectoryConstants;
import com.keypoint.keypointtravel.global.enumType.error.BlockedMemberErrorCode;
import com.keypoint.keypointtravel.global.enumType.error.CampaignErrorCode;
import com.keypoint.keypointtravel.global.enumType.error.MemberErrorCode;
import com.keypoint.keypointtravel.global.exception.GeneralException;
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
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EditCampaignService {

    private final CampaignBudgetRepository campaignBudgetRepository;

    private final MemberRepository memberRepository;

    private final MemberCampaignRepository memberCampaignRepository;

    private final CampaignRepository campaignRepository;

    private final BlockedMemberRepository blockedMemberRepository;

    private final TravelLocationRepository travelLocationRepository;

    private final UploadFileService uploadFileService;

    private final CustomMemberCampaignRepository customMemberCampaignRepository;

    /**
     * 수정을 위한 캠페인 조회 함수
     *
     * @Param campaignId, memberId useCase
     * @Return EditCampaignResponse
     */
    @Transactional(readOnly = true)
    public EditCampaignResponse findCampaign(FIndCampaignUseCase useCase) {
        // 캠페인 장인지 확인 필요
        if (!customMemberCampaignRepository.existsByCampaignLeaderTrue(useCase.getMemberId(),
            useCase.getCampaignId())) {
            throw new GeneralException(CampaignErrorCode.NOT_CAMPAIGN_OWNER);
        }
        // 캠페인 id, 이미지 url, 제목, 시작일, 종료일 조회
        CampaignInfoDto campaign = campaignRepository.findCampaignInfo(useCase.getCampaignId());
        // 여행지 리스트 조회
        List<TravelLocationDto> travelLocations = campaignRepository.findTravelLocationList(useCase.getCampaignId());
        // 예산 리스트 조회
        List<CampaignBudget> campaignBudgets = campaignBudgetRepository.findAllByCampaignId(useCase.getCampaignId());
        // 참여인원 리스트 조회
        List<MemberInfoDto> dtoList = memberRepository.findCampaignMemberList(useCase.getCampaignId());
        // 캠페인 장 제외
        dtoList.removeIf(memberInfoDto -> memberInfoDto.getMemberId().equals(useCase.getMemberId()));
        return new EditCampaignResponse(campaign, travelLocations, campaignBudgets, dtoList);
    }

    /**
     * 캠페인 수정 함수
     *
     * @Param 캠페인 아이디, 커버 이미지, 여행지 정보, 회원 정보, 예산 정보 등 캠페인 생성에 필요한 정보 useCase
     */
    @Transactional
    public void editCampaign(UpdateUseCase useCase) {
        // 1. 캠페인 장인지 확인 필요
        if (!customMemberCampaignRepository.existsByCampaignLeaderTrue(useCase.getMemberId(),
            useCase.getCampaignId())) {
            throw new GeneralException(CampaignErrorCode.NOT_CAMPAIGN_OWNER);
        }
        // 2. 참여한 캠페인 수 및 프리미엄 회원인지 검증
        validatePremiumMember(useCase);
        // 3. 참여 회원 중 캠페인 기간이 겹치는 다른 캠페인이 있는지 검증
        validatePeriodOverlap(useCase);
        // 4. 서로 차단한 회원이 있는지 검증
        if(useCase.getMembers() != null && !useCase.getMembers().isEmpty()){
            useCase.getMembers().add(new MemberInfo(useCase.getMemberId()));
            validateBlockedMembers(useCase.getMembers());
        }
        // 5. 커버 사진 업데이트
        Campaign campaign = campaignRepository.findById(useCase.getCampaignId())
            .orElseThrow(() -> new GeneralException(CampaignErrorCode.NOT_EXISTED_CAMPAIGN));
        updateUploadFile(campaign.getCampaignImageId(), useCase.getCoverImage());
        // 6. 켐페인 정보 업데이트
        campaign.updateInfo(useCase.getTitle(), useCase.getStartDate(), useCase.getEndDate());
        // 7. 켐페인 예산 업데이트
        updateCampaignBudgets(campaign, useCase.getBudgets());
        // 8. 회원 캠페인 업데이트
        updateMemberCampaigns(campaign, useCase.getMembers(), useCase.getMemberId());
        // 9. 여행지 업데이트
        updateTravelLocations(campaign, useCase.getTravels());
    }

    private void validatePremiumMember(UpdateUseCase useCase){
        List<Long> memberIds = new ArrayList<>();
        if(useCase.getMembers() != null && !useCase.getMembers().isEmpty()){
            memberIds = useCase.getMembers().stream()
                .map(MemberInfo::getMemberId)
                .collect(Collectors.toList());
        }
        memberIds.add(useCase.getMemberId());
        // 가입한 캠페인 수가 1개 이상이지만 프리미엄 회원이 아닌지 검증
        if(customMemberCampaignRepository.existsMultipleCampaignNotPremium(memberIds)){
            throw new GeneralException(CampaignErrorCode.MULTIPLE_CAMPAIGN_NON_PREMIUM);
        }
    }

    private void validatePeriodOverlap(UpdateUseCase useCase) {
        // 회원 중 기간이 겹치는 다른 캠페인이 있는지 검증
        List<Long> memberIds = new ArrayList<>();
        if (useCase.getMembers() != null && !useCase.getMembers().isEmpty()) {
            memberIds = useCase.getMembers().stream()
                .map(MemberInfo::getMemberId)
                .collect(Collectors.toList());
        }
        memberIds.add(useCase.getMemberId());
        if (campaignRepository.existsOverlappingCampaign(memberIds, useCase.getStartDate(), useCase.getEndDate())) {
            throw new GeneralException(CampaignErrorCode.CAMPAIGN_PERIOD_OVERLAP);
        }
    }

    private void validateBlockedMembers(List<MemberInfo> members) {
        List<Long> memberIds = members.stream()
            .map(MemberInfo::getMemberId)
            .toList();
        if (blockedMemberRepository.existsBlockedMembers(memberIds)) {
            throw new GeneralException(BlockedMemberErrorCode.EXISTS_BLOCKED_MEMBER);
        }
    }

    private void updateUploadFile(Long uploadFileId, MultipartFile coverImage) {
        try {
            uploadFileService.updateUploadFile(uploadFileId, coverImage,
                DirectoryConstants.CAMPAIGN_COVER_DIRECTORY);
        } catch (IOException e) {
            throw new GeneralException(e);
        }
    }

    private void updateCampaignBudgets(Campaign campaign, List<BudgetInfo> budgets) {
        campaignBudgetRepository.deleteAllByCampaignId(campaign.getId());
        List<CampaignBudget> campaignBudgets = budgets.stream()
            .map(budget -> CampaignBudget.builder()
                .campaign(campaign)
                .category(budget.getCategory())
                .amount(budget.getAmount())
                .currency(budget.getCurrency())
                .build()).toList();
        campaignBudgetRepository.saveAll(campaignBudgets);
    }

    private void updateMemberCampaigns(Campaign campaign, List<MemberInfo> members, Long leaderId) {
        memberCampaignRepository.deleteAllByCampaignId(campaign.getId());
        List<MemberCampaign> memberCampaigns = new ArrayList<>();
        // 함께 참여하는 회원들 저장
        if (members != null && !members.isEmpty()) {
            for (MemberInfo memberInfo : members) {
                if (memberInfo.getMemberId().equals(leaderId)) {
                    continue;
                }
                Member member = memberRepository.getReferenceById(memberInfo.getMemberId());
                memberCampaigns.add(new MemberCampaign(campaign, member, false));
            }
        }
        // 캠페인을 생성하는 회원(캠페인 장) 저장
        Member member = memberRepository.getReferenceById(leaderId);
        memberCampaigns.add(new MemberCampaign(campaign, member, true));
        try {
            memberCampaignRepository.saveAll(memberCampaigns);
        } catch (Exception e) {
            throw new GeneralException(MemberErrorCode.NOT_EXISTED_MEMBER);
        }
    }

    private void updateTravelLocations(Campaign campaign, List<TravelInfo> travels) {
        travelLocationRepository.deleteAllByCampaignId(campaign.getId());
        List<TravelLocation> travelLocations = travels.stream()
            .map(travel -> new TravelLocation(campaign, travel.getPlaceId(), travel.getSequence()))
            .toList();
        travelLocationRepository.saveAll(travelLocations);
    }
}
