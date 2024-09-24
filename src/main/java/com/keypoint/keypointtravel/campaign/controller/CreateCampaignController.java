package com.keypoint.keypointtravel.campaign.controller;

import com.keypoint.keypointtravel.campaign.dto.request.createRequest.BudgetInfo;
import com.keypoint.keypointtravel.campaign.dto.request.createRequest.CreateRequest;
import com.keypoint.keypointtravel.campaign.dto.request.createRequest.EmailInfo;
import com.keypoint.keypointtravel.campaign.dto.useCase.CreateUseCase;
import com.keypoint.keypointtravel.campaign.dto.useCase.InviteByEmailsUseCase;
import com.keypoint.keypointtravel.campaign.service.CreateCampaignService;
import com.keypoint.keypointtravel.global.config.security.CustomUserDetails;
import com.keypoint.keypointtravel.global.enumType.error.CampaignErrorCode;
import com.keypoint.keypointtravel.global.exception.GeneralException;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Locale;
import lombok.RequiredArgsConstructor;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/campaigns")
@RequiredArgsConstructor
public class CreateCampaignController {

    private final CreateCampaignService createCampaignService;

    @PreAuthorize("hasRole('ROLE_CERTIFIED_USER')")
    @PostMapping
    public ResponseEntity<Void> saveCampaign(
        @RequestPart(value = "coverImage", required = false) MultipartFile coverImage,
        @RequestPart(value = "detail") @Valid CreateRequest request,
        @AuthenticationPrincipal CustomUserDetails userDetails) {
        // 총 예산과 카테고리별 합산 예산이 일치하지 않는 경우 예외 처리
        if (request.getBudgets().stream().mapToDouble(BudgetInfo::getAmount).sum()
            != request.getTotalBudget()) {
            throw new GeneralException(CampaignErrorCode.NOT_MATCH_BUDGET);
        }
        // 이메일 초대 명단에 자기 자신이 있는지 확인
        if (request.getEmails() != null && !request.getEmails().isEmpty()) {
            validateInviteSelf(request.getEmails(), userDetails.getEmail());
        }
        // 캠페인 생성
        Long campaignId = createCampaignService.createCampaign(
            CreateUseCase.of(coverImage, request, userDetails.getId()));
        // 이메일로 초대
        if (request.getEmails() != null && !request.getEmails().isEmpty()) {
            Locale locale = LocaleContextHolder.getLocale();
            createCampaignService.sendEmail(
                InviteByEmailsUseCase.of(request.getEmails(), userDetails.getId(), campaignId),
                locale);
        }
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    private void validateInviteSelf(List<EmailInfo> emails, String leaderEmail) {
        for (EmailInfo email : emails) {
            if (leaderEmail.equals(email.getEmail())) {
                throw new GeneralException(CampaignErrorCode.CANNOT_INVITE_SELF);
            }
        }
    }
}
