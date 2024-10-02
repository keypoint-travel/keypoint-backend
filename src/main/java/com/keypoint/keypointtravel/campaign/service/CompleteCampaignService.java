package com.keypoint.keypointtravel.campaign.service;

import com.keypoint.keypointtravel.campaign.dto.dto.CampaignDto;
import com.keypoint.keypointtravel.campaign.dto.dto.CampaignReportDto;
import com.keypoint.keypointtravel.campaign.dto.response.CampaignResponse;
import com.keypoint.keypointtravel.campaign.dto.useCase.CampaignReportUseCase;
import com.keypoint.keypointtravel.campaign.dto.useCase.CompleteCampaignUseCase;
import com.keypoint.keypointtravel.campaign.dto.useCase.FIndCampaignListUseCase;
import com.keypoint.keypointtravel.campaign.entity.Campaign;
import com.keypoint.keypointtravel.campaign.entity.CampaignReport;
import com.keypoint.keypointtravel.campaign.entity.EmailInvitationHistory;
import com.keypoint.keypointtravel.campaign.entity.InvitationProhibitionHistory;
import com.keypoint.keypointtravel.campaign.entity.MemberCampaign;
import com.keypoint.keypointtravel.campaign.repository.CampaignReportRepository;
import com.keypoint.keypointtravel.campaign.repository.CustomCampaignReportRepository;
import com.keypoint.keypointtravel.campaign.repository.CampaignRepository;
import com.keypoint.keypointtravel.campaign.repository.CampaignWaitMemberRepository;
import com.keypoint.keypointtravel.campaign.repository.EmailInvitationHistoryRepository;
import com.keypoint.keypointtravel.campaign.repository.InvitationProhibitionHistoryRepository;
import com.keypoint.keypointtravel.campaign.repository.MemberCampaignRepository;
import com.keypoint.keypointtravel.external.aws.service.S3Service;
import com.keypoint.keypointtravel.global.constants.DirectoryConstants;
import com.keypoint.keypointtravel.global.entity.UploadFile;
import com.keypoint.keypointtravel.global.enumType.campaign.Status;
import com.keypoint.keypointtravel.global.enumType.email.EmailTemplate;
import com.keypoint.keypointtravel.global.enumType.error.CampaignErrorCode;
import com.keypoint.keypointtravel.global.enumType.notification.PushNotificationType;
import com.keypoint.keypointtravel.global.exception.GeneralException;
import com.keypoint.keypointtravel.notification.event.pushNotification.CampaignPushNotificationEvent;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import com.keypoint.keypointtravel.global.utils.EmailUtils;
import com.keypoint.keypointtravel.uploadFile.service.UploadFileService;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CompleteCampaignService {

    private final static PushNotificationType PUSH_NOTIFICATION_TYPE = PushNotificationType.CAMPAIGN_END;

    private final MemberCampaignRepository memberCampaignRepository;

    private final CampaignRepository campaignRepository;

    private final CampaignWaitMemberRepository campaignWaitMemberRepository;

    private final ApplicationEventPublisher eventPublisher;

    private final UploadFileService uploadFileService;

    private final CustomCampaignReportRepository customCampaignReportRepository;

    private final CampaignReportRepository campaignReportRepository;

    private final S3Service s3Service;

    private final EmailInvitationHistoryRepository emailInvitationHistoryRepository;

    private final InvitationProhibitionHistoryRepository invitationProhibitionHistoryRepository;

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
        // 3. 캠페인 대기 목록 삭제
        campaignWaitMemberRepository.deleteAllByCampaignId(useCase.getCampaignId());
        // 4. 해당 캠페인 관련 Redis 내부 항목 제거
        List<EmailInvitationHistory> emailInvitationHistories = emailInvitationHistoryRepository.findAllByCampaignId(
            useCase.getCampaignId());
        emailInvitationHistoryRepository.deleteAll(emailInvitationHistories);
        List<InvitationProhibitionHistory> histories = invitationProhibitionHistoryRepository.findAllByCampaignId(
            useCase.getCampaignId());
        invitationProhibitionHistoryRepository.deleteAll(histories);
        // 5. 캠페인 종료 알림 전송, 배지 부여
        List<Long> invitedMemberIds = memberCampaignRepository.findMemberIdsByCampaignId(
            useCase.getCampaignId()
        );
        eventPublisher.publishEvent(CampaignPushNotificationEvent.of(
                PUSH_NOTIFICATION_TYPE,
                invitedMemberIds,
                useCase.getCampaignId()
            )
        );
    }

    private List<Long> validateIsLeader(Long memberId, Long campaignId) {
        List<MemberCampaign> memberCampaigns = memberCampaignRepository.findAllByCampaignId(
            campaignId);
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

    /**
     * 종료된 캠페인 목록 조회 함수
     *
     * @Param memberId useCase
     * @Return List<CampaignInfoDto>
     */
    @Transactional(readOnly = true)
    public Page<CampaignResponse> findCampaigns(FIndCampaignListUseCase useCase) {
        // 1. 캠페인 정보 조회
        CampaignDto dto = campaignRepository.findCampaignInfoList(
            useCase.getMemberId(), Status.FINISHED, useCase.getSize(), useCase.getPage());
        // 2. 페이지 형태로 반환
        Sort sort = Sort.by(Sort.Direction.DESC, "id");
        Pageable pageable = PageRequest.of(
            useCase.getPage() > 0 ? useCase.getPage() - 1 : 0,
            useCase.getSize() > 0 ? useCase.getSize() : 1, sort);
        return new PageImpl<>(CampaignResponse.from(dto.getDtoList()), pageable,
            dto.getTotalCount());
    }

    /**
     * 캠페인 레포트 이메일 전송 전 검증 함수
     *
     * @Param campaignId, email, memberId, reportImage useCase
     */
    @Transactional
    public void validateCampaignMember(CampaignReportUseCase useCase) {
        // 1. 캠페인에 소속되어 있는지 검증
        if (!memberCampaignRepository.existsByCampaignIdAndMemberId(useCase.getCampaignId(),
            useCase.getMemberId())) {
            throw new GeneralException(CampaignErrorCode.NOT_EXISTED_CAMPAIGN);
        }
    }

    /**
     * 캠페인 레포트 이메일 전송 함수
     *
     * @Param email, campaignId, reportImage useCase
     */
    @Async
    @Transactional
    public void sendCampaignReportEmail(CampaignReportUseCase useCase, Locale locale) {
        LocaleContextHolder.setLocale(locale);
        // 1. 이미 캠페인 레포트 이미지가 저장되어 있는지 확인
        String filePath;
        if (customCampaignReportRepository.existsByCampaignId(useCase.getCampaignId())) {
            // DB에서 이미지 조회
            filePath = customCampaignReportRepository.findReportImageUrl(useCase.getCampaignId());
        } else {
            // 레포트 이미지 업로드
            filePath = saveReportImage(useCase);
        }
        // 2. 이미지 url로 유효 기간(단위 : 시간)동안 다운로드 가능한 링크 생성
        String url = createDownloadUrl(filePath, useCase.getReportImage().getOriginalFilename());
        // 3. 수신자 이름, 캠페인 이름 조회
        CampaignReportDto dto = customCampaignReportRepository.findCampaignReportInfo(
            useCase.getCampaignId(), useCase.getMemberId());
        // 4. 이메일 전송
        sendEmail(dto.getCampaignName(), dto.getMemberName(),
            useCase.getReportImage().getOriginalFilename(), url, useCase.getEmail());
    }

    private String saveReportImage(CampaignReportUseCase useCase) {
        try {
            UploadFile uploadFile = uploadFileService.saveAndReturnUploadFile(
                useCase.getReportImage(), DirectoryConstants.CAMPAIGN_REPORT_DIRECTORY);
            Campaign campaign = campaignRepository.getReferenceById(useCase.getCampaignId());
            CampaignReport campaignReport = new CampaignReport(campaign, uploadFile.getId());
            campaignReportRepository.save(campaignReport);
            return uploadFile.getPath();
        } catch (Exception e) {
            throw new GeneralException(e);
        }
    }

    private String createDownloadUrl(String filePath, String fileName) {
        try {
            return s3Service.generatePreSignedUrl(filePath, 48L, fileName);
        } catch (Exception e) {
            throw new GeneralException(e);
        }
    }

    private void sendEmail(String campaignName, String memberName, String reportFileName,
        String downloadUrl, String email) {
        Map<String, String> emailContent = new HashMap<>();
        emailContent.put("campaignName", campaignName);
        emailContent.put("memberName", memberName);
        emailContent.put("reportFileName", reportFileName);
        emailContent.put("downloadUrl", downloadUrl);
        List<String> images = new ArrayList<>();
        images.add("static/images/main-logo.jpg");
        EmailUtils.sendSingleEmailWithImages(
            email, EmailTemplate.CAMPAIGN_REPORT, new Object[]{campaignName}, emailContent, images);
    }
}
