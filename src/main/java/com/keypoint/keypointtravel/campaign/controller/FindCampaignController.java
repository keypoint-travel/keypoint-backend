package com.keypoint.keypointtravel.campaign.controller;

import com.keypoint.keypointtravel.campaign.dto.dto.CampaignInfoDto;
import com.keypoint.keypointtravel.campaign.dto.response.CampaignResponse;
import com.keypoint.keypointtravel.campaign.dto.response.PercentageResponse;
import com.keypoint.keypointtravel.campaign.dto.response.PaymentResponse;
import com.keypoint.keypointtravel.campaign.dto.response.details.CampaignDetailsResponse;
import com.keypoint.keypointtravel.campaign.dto.response.member.PercentageByMemberResponse;
import com.keypoint.keypointtravel.campaign.dto.response.member.TotalAmountByMemberResponse;
import com.keypoint.keypointtravel.campaign.dto.useCase.FIndCampaignUseCase;
import com.keypoint.keypointtravel.campaign.dto.useCase.FindCampaignMemberUseCase;
import com.keypoint.keypointtravel.campaign.dto.useCase.FindPaymentsUseCase;
import com.keypoint.keypointtravel.campaign.dto.useCase.FindPercentangeUseCase;
import com.keypoint.keypointtravel.campaign.service.FindCampaignService;
import com.keypoint.keypointtravel.campaign.service.FindPaymentService;
import com.keypoint.keypointtravel.campaign.service.FindPercentageService;
import com.keypoint.keypointtravel.global.config.security.CustomUserDetails;
import com.keypoint.keypointtravel.global.dto.response.APIResponseEntity;
import com.keypoint.keypointtravel.global.dto.response.PageResponse;
import com.keypoint.keypointtravel.global.enumType.receipt.ReceiptCategory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/campaigns")
@RequiredArgsConstructor
public class FindCampaignController {

    private final FindPercentageService findPercentageService;

    private final FindCampaignService findCampaignService;

    private final FindPaymentService findPaymentService;

    @PreAuthorize("hasRole('ROLE_CERTIFIED_USER')")
    @GetMapping("/{campaignId}")
    public APIResponseEntity<CampaignResponse> findCampaigns(
        @RequestParam(value = "currency", defaultValue = "null") String currencyType,
        @PathVariable Long campaignId,
        @AuthenticationPrincipal CustomUserDetails userDetails) {
        // 캠페인 정보 조회
        FIndCampaignUseCase useCase = new FIndCampaignUseCase(campaignId, userDetails.getId());
        CampaignInfoDto dto = findCampaignService.findCampaigns(useCase);
        // 카테고리별 비율 조회
        FindPercentangeUseCase findPercentagesUseCase =
            new FindPercentangeUseCase(campaignId, userDetails.getId(), currencyType);
        PercentageResponse temp = findPercentageService.findCategoryPercentage(findPercentagesUseCase);
        // 응답
        CampaignResponse response = CampaignResponse.of(dto, temp.getPercentages());
        return APIResponseEntity.<CampaignResponse>builder()
            .message("메인화면 캠페인 목록 조회 성공")
            .data(response)
            .build();
    }

    @PreAuthorize("hasRole('ROLE_CERTIFIED_USER')")
    @GetMapping("/details/{campaignId}")
    public APIResponseEntity<CampaignDetailsResponse> findCampaignDetails(
        @PathVariable Long campaignId,
        @AuthenticationPrincipal CustomUserDetails userDetails) {
        FIndCampaignUseCase useCase = new FIndCampaignUseCase(campaignId, userDetails.getId());
        CampaignDetailsResponse response = findCampaignService.findCampaignDetails(useCase);
        return APIResponseEntity.<CampaignDetailsResponse>builder()
            .message("캠페인 상세 페이지 상단 조회 성공")
            .data(response)
            .build();
    }

    @PreAuthorize("hasRole('ROLE_CERTIFIED_USER')")
    @GetMapping("/details/{campaignId}/percentage/category")
    public APIResponseEntity<PercentageResponse> findCampaignCategoryPercentages(
        @RequestParam(value = "currency", defaultValue = "null") String currencyType,
        @PathVariable Long campaignId,
        @AuthenticationPrincipal CustomUserDetails userDetails) {
        FindPercentangeUseCase useCase = new FindPercentangeUseCase(campaignId, userDetails.getId(), currencyType);
        PercentageResponse response = findPercentageService.findCategoryPercentage(useCase);
        return APIResponseEntity.<PercentageResponse>builder()
            .message("캠페인 카테고리별 비율 조회 성공")
            .data(response)
            .build();
    }

    @PreAuthorize("hasRole('ROLE_CERTIFIED_USER')")
    @GetMapping("/details/{campaignId}/payment/category")
    public APIResponseEntity<PageResponse<PaymentResponse>> findCampaignCategoryPayments(
        @RequestParam(value = "currency", defaultValue = "null") String currencyType,
        @RequestParam(value = "size", defaultValue = "10") int size,
        @RequestParam(value = "page", defaultValue = "1") int page,
        @RequestParam("category") ReceiptCategory category,
        @PathVariable Long campaignId,
        @AuthenticationPrincipal CustomUserDetails userDetails) {
        FindPaymentsUseCase useCase = new FindPaymentsUseCase(campaignId, userDetails.getId(), currencyType, size, page);
        Page<PaymentResponse> response = findPaymentService.findPaymentsByCategory(useCase, category);
        return APIResponseEntity.toPage(
            "캠페인 카테고리별 결제 내역 조회 성공",
            response
        );
    }

    @PreAuthorize("hasRole('ROLE_CERTIFIED_USER')")
    @GetMapping("/details/{campaignId}/percentage/date")
    public APIResponseEntity<PercentageResponse> findCampaignDatePercentages(
        @RequestParam(value = "currency", defaultValue = "null") String currencyType,
        @PathVariable Long campaignId,
        @AuthenticationPrincipal CustomUserDetails userDetails) {
        FindPercentangeUseCase useCase = new FindPercentangeUseCase(campaignId, userDetails.getId(), currencyType);
        PercentageResponse response = findPercentageService.findDatePercentage(useCase);
        response.sortPercentages();
        return APIResponseEntity.<PercentageResponse>builder()
            .message("캠페인 날짜별 비율 조회 성공")
            .data(response)
            .build();
    }

    @PreAuthorize("hasRole('ROLE_CERTIFIED_USER')")
    @GetMapping("/details/{campaignId}/payment/date")
    public APIResponseEntity<PageResponse<PaymentResponse>> findCampaignDatePayments(
        @RequestParam(value = "currency", defaultValue = "null") String currencyType,
        @RequestParam(value = "size", defaultValue = "10") int size,
        @RequestParam(value = "page", defaultValue = "1") int page,
        @RequestParam(value = "date", defaultValue = "null") String date,
        @PathVariable Long campaignId,
        @AuthenticationPrincipal CustomUserDetails userDetails) {
        FindPaymentsUseCase useCase = new FindPaymentsUseCase(campaignId, userDetails.getId(), currencyType, size, page);
        Page<PaymentResponse> response = findPaymentService.findPaymentsByDate(useCase, date);
        return APIResponseEntity.toPage(
            "캠페인 날짜별 결제 내역 조회 성공",
            response
        );
    }

    @PreAuthorize("hasRole('ROLE_CERTIFIED_USER')")
    @GetMapping("/details/{campaignId}/payment/price")
    public APIResponseEntity<PageResponse<PaymentResponse>> findCampaignPricePayments(
        @RequestParam(value = "currency", defaultValue = "null") String currencyType,
        @RequestParam(value = "size", defaultValue = "10") int size,
        @RequestParam(value = "page", defaultValue = "1") int page,
        @RequestParam(value = "direction", defaultValue = "DESC") Direction order,
        @PathVariable Long campaignId,
        @AuthenticationPrincipal CustomUserDetails userDetails) {
        FindPaymentsUseCase useCase = new FindPaymentsUseCase(campaignId, userDetails.getId(), currencyType, size, page);
        Page<PaymentResponse> response = findPaymentService.findPaymentsByDate(useCase, order);
        return APIResponseEntity.toPage(
            "캠페인 금액별 결제 내역 조회 성공",
            response
        );
    }

    @PreAuthorize("hasRole('ROLE_CERTIFIED_USER')")
    @GetMapping("/details/{campaignId}/member")
    public APIResponseEntity<TotalAmountByMemberResponse> findCampaignMemberTotalAmount(
        @PathVariable Long campaignId,
        @AuthenticationPrincipal CustomUserDetails userDetails) {
        TotalAmountByMemberResponse response = findPaymentService.findTotalPaymentsByAllMember(campaignId);
        return APIResponseEntity.<TotalAmountByMemberResponse>builder()
            .message("캠페인 회원별 총 금액 조회 성공")
            .data(response)
            .build();
    }

    @PreAuthorize("hasRole('ROLE_CERTIFIED_USER')")
    @GetMapping("/details/{campaignId}/percentage/member/{memberId}")
    public APIResponseEntity<PercentageByMemberResponse> findCampaignMemberPercentages(
        @PathVariable Long campaignId,
        @PathVariable Long memberId,
        @AuthenticationPrincipal CustomUserDetails userDetails) {
        FindCampaignMemberUseCase useCase = new FindCampaignMemberUseCase(campaignId, memberId);
        PercentageByMemberResponse response = findPercentageService.findMemberPercentage(useCase);
        return APIResponseEntity.<PercentageByMemberResponse>builder()
            .message("캠페인 회원 카테고리별 비율 조회 성공")
            .data(response)
            .build();
    }

    @PreAuthorize("hasRole('ROLE_CERTIFIED_USER')")
    @GetMapping("/details/{campaignId}/payment/member")
    public APIResponseEntity<PageResponse<PaymentResponse>> findCampaignMemberPayments(
        @RequestParam(value = "currency", defaultValue = "null") String currencyType,
        @RequestParam(value = "size", defaultValue = "10") int size,
        @RequestParam(value = "page", defaultValue = "1") int page,
        @RequestParam(value = "memberId", defaultValue = "1") Long memberId,
        @PathVariable Long campaignId,
        @AuthenticationPrincipal CustomUserDetails userDetails) {
        FindPaymentsUseCase useCase = new FindPaymentsUseCase(campaignId, memberId, currencyType, size, page);
        Page<PaymentResponse> response = findPaymentService.findPaymentsByMember(useCase);
        return APIResponseEntity.toPage(
            "캠페인 회원별 결제 내역 조회 성공",
            response
        );
    }
}
