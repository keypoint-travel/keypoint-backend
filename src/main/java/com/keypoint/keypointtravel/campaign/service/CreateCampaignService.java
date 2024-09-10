package com.keypoint.keypointtravel.campaign.service;

import com.keypoint.keypointtravel.blocked_member.repository.BlockedMemberRepository;
import com.keypoint.keypointtravel.campaign.entity.EmailInvitationHistory;
import com.keypoint.keypointtravel.campaign.dto.dto.SendInvitationEmailDto;
import com.keypoint.keypointtravel.campaign.dto.request.MemberInfo;
import com.keypoint.keypointtravel.campaign.dto.useCase.CreateUseCase;
import com.keypoint.keypointtravel.campaign.dto.useCase.InviteByEmailsUseCase;
import com.keypoint.keypointtravel.campaign.entity.Campaign;
import com.keypoint.keypointtravel.campaign.entity.CampaignBudget;
import com.keypoint.keypointtravel.campaign.entity.MemberCampaign;
import com.keypoint.keypointtravel.campaign.entity.TravelLocation;
import com.keypoint.keypointtravel.campaign.repository.*;
import com.keypoint.keypointtravel.global.constants.DirectoryConstants;
import com.keypoint.keypointtravel.global.enumType.campaign.Status;
import com.keypoint.keypointtravel.global.enumType.email.EmailTemplate;
import com.keypoint.keypointtravel.global.enumType.error.BlockedMemberErrorCode;
import com.keypoint.keypointtravel.global.enumType.error.CampaignErrorCode;
import com.keypoint.keypointtravel.global.enumType.error.MemberErrorCode;
import com.keypoint.keypointtravel.global.exception.GeneralException;
import com.keypoint.keypointtravel.global.utils.EmailUtils;
import com.keypoint.keypointtravel.global.utils.StringUtils;
import com.keypoint.keypointtravel.member.entity.Member;
import com.keypoint.keypointtravel.member.repository.member.MemberRepository;
import com.keypoint.keypointtravel.uploadFile.service.UploadFileService;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CreateCampaignService {

    private final CampaignRepository campaignRepository;

    private final CampaignBudgetRepository campaignBudgetRepository;

    private final MemberCampaignRepository memberCampaignRepository;

    private final MemberRepository memberRepository;

    private final TravelLocationRepository travelLocationRepository;

    private final BlockedMemberRepository blockedMemberRepository;

    private final UploadFileService uploadFileService;

    private final EmailInvitationHistoryRepository emailInvitationHistoryRepository;

    private final CustomMemberCampaignRepository customMemberCampaignRepository;

    /**
     * 캠페인 생성 함수
     *
     * @Param 커버 이미지, 캠페인 정보, 회원 정보, 예산 정보 등 캠페인 생성에 필요한 정보 useCase
     */
    @Transactional
    public Long createCampaign(CreateUseCase useCase) {
        // 0. 서로 차단한 회원이 있는지 검증
        if (useCase.getMembers() != null && !useCase.getMembers().isEmpty()) {
            useCase.getMembers().add(new MemberInfo(useCase.getMemberId()));
            validateBlockedMembers(useCase.getMembers());
        }
        // 1. 참여한 캠페인 수 및 프리미엄 회원인지 검증
        validatePremiumMember(useCase);
        // 2. 커버 사진 upload File 저장
        Long coverImageId = saveUploadFile(useCase.getCoverImage());
        // 3. 켐페인 저장
        Campaign campaign = saveCampaign(useCase, coverImageId);
        // 4. 켐페인 예산 저장
        saveCampaignBudgets(campaign, useCase);
        // 5. 회원 캠페인 저장
        saveMemberCampaigns(campaign, useCase);
        // 6. 여행지 저장
        saveTravelLocations(campaign, useCase);
        // todo : 알림 기능 추가 예정
        return campaign.getId();
    }

    private void validateBlockedMembers(List<MemberInfo> members) {
        List<Long> memberIds = members.stream()
            .map(MemberInfo::getMemberId)
            .toList();
        if (blockedMemberRepository.existsBlockedMembers(memberIds)) {
            throw new GeneralException(BlockedMemberErrorCode.EXISTS_BLOCKED_MEMBER);
        }
    }

    private void validatePremiumMember(CreateUseCase useCase) {
        List<Long> memberIds = new ArrayList<>();
        if (useCase.getMembers() != null && !useCase.getMembers().isEmpty()) {
            memberIds = useCase.getMembers().stream()
                .map(MemberInfo::getMemberId)
                .collect(Collectors.toList());
        }
        memberIds.add(useCase.getMemberId());
        // 가입한 캠페인 수가 1개 이상이지만 프리미엄 회원이 아닌지 검증
        if (customMemberCampaignRepository.existsMultipleCampaignNotPremium(memberIds)) {
            throw new GeneralException(CampaignErrorCode.MULTIPLE_CAMPAIGN_NON_PREMIUM);
        }
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
        if (useCase.getMembers() != null && !useCase.getMembers().isEmpty()) {
            for (MemberInfo memberInfo : useCase.getMembers()) {
                if (memberInfo.getMemberId().equals(useCase.getMemberId())) {
                    continue;
                }
                Member member = memberRepository.getReferenceById(memberInfo.getMemberId());
                memberCampaigns.add(new MemberCampaign(campaign, member, false));
            }
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


    /**
     * 캠페인 이메일 초대 함수(이메일 전송, redis 이메일 저장)
     *
     * @Param emails, memberId(요청을 보낸 사용자), campaignId useCase
     */
    @Async
    @Transactional
    public void sendEmail(InviteByEmailsUseCase useCase, Locale locale) {
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
        EmailUtils.sendMultiEmailWithImages(
            useCase.getEmails(), EmailTemplate.INVITE_CAMPAIGN, emailContent, images);

        // 캠페인 이메일 초대 기록 Redis 에 저장(하루의 만료기간 설정)
        List<EmailInvitationHistory> histories = new ArrayList<>();
        for (String email : useCase.getEmails()) {
            histories.add(new EmailInvitationHistory(useCase.getCampaignId(), email,
                useCase.getCampaignId()));
        }
        emailInvitationHistoryRepository.saveAll(histories);
    }
}
