package com.keypoint.keypointtravel.inquiry.controller;


import com.keypoint.keypointtravel.global.config.security.CustomUserDetails;
import com.keypoint.keypointtravel.global.dto.response.APIResponseEntity;
import com.keypoint.keypointtravel.inquiry.dto.request.InquiryRequest;
import com.keypoint.keypointtravel.inquiry.dto.response.UserInquiriesResponse;
import com.keypoint.keypointtravel.inquiry.dto.response.UserInquiryResponse;
import com.keypoint.keypointtravel.inquiry.dto.useCase.DeleteUseCase;
import com.keypoint.keypointtravel.inquiry.dto.useCase.FindUserInquiryUseCase;
import com.keypoint.keypointtravel.inquiry.dto.useCase.InquiryUseCase;
import com.keypoint.keypointtravel.inquiry.dto.useCase.ReplyUseCase;
import com.keypoint.keypointtravel.inquiry.service.UserInquiryService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/v1/inquiries")
@RequiredArgsConstructor
public class UserInquiryController {

    private final UserInquiryService userInquiryService;

    // 1:1 새 문의하기
    @PostMapping
    @PreAuthorize("hasRole('ROLE_CERTIFIED_USER')")
    public APIResponseEntity<Void> inquire(
        @RequestPart(value = "images", required = false) List<MultipartFile> images,
        @RequestPart(value = "content") InquiryRequest request,
        @AuthenticationPrincipal CustomUserDetails userDetails) {
        InquiryUseCase useCase = new InquiryUseCase(request.getContent(), images, userDetails.getId());
        userInquiryService.inquire(useCase);
        return APIResponseEntity.<Void>builder()
            .message("문의하기 성공")
            .build();
    }

    // 1:1 추가 문의(답변)
    @PostMapping("/{inquiryId}")
    @PreAuthorize("hasRole('ROLE_CERTIFIED_USER')")
    public APIResponseEntity<Void> addInquire(
        @PathVariable Long inquiryId,
        @RequestPart(value = "images", required = false) List<MultipartFile> images,
        @RequestPart(value = "content") InquiryRequest request,
        @AuthenticationPrincipal CustomUserDetails userDetails) {
        ReplyUseCase useCase = new ReplyUseCase(inquiryId, request.getContent(), images, userDetails.getId());
        userInquiryService.answer(useCase);
        return APIResponseEntity.<Void>builder()
            .message("추가 문의하기 성공")
            .build();
    }

    // 1:1 문의 목록 조회
    @GetMapping
    @PreAuthorize("hasRole('ROLE_CERTIFIED_USER')")
    public APIResponseEntity<List<UserInquiriesResponse>> findInquiries(
        @AuthenticationPrincipal CustomUserDetails userDetails) {
        List<UserInquiriesResponse> inquiries = userInquiryService.findInquiries(userDetails.getId());
        return APIResponseEntity.<List<UserInquiriesResponse>>builder()
            .data(inquiries)
            .message("문의 목록 조회 성공")
            .build();
    }

    // 1:1 문의 상세 조회
    @GetMapping("/{inquiryId}")
    @PreAuthorize("hasRole('ROLE_CERTIFIED_USER')")
    public APIResponseEntity<UserInquiryResponse> findInquiry(
        @PathVariable Long inquiryId,
        @AuthenticationPrincipal CustomUserDetails userDetails) {
        FindUserInquiryUseCase useCase = new FindUserInquiryUseCase(inquiryId, userDetails.getId());
        UserInquiryResponse response = userInquiryService.findInquiry(useCase);
        return APIResponseEntity.<UserInquiryResponse>builder()
            .data(response)
            .message("문의 상세 조회 성공")
            .build();
    }

    // 1:1 문의 삭제
    @DeleteMapping("/{inquiryId}")
    @PreAuthorize("hasRole('ROLE_CERTIFIED_USER')")
    public APIResponseEntity<Void> deleteInquire(
        @PathVariable Long inquiryId,
        @AuthenticationPrincipal CustomUserDetails userDetails) {
        DeleteUseCase useCase = new DeleteUseCase(inquiryId, userDetails.getId());
        userInquiryService.delete(useCase);
        return APIResponseEntity.<Void>builder()
            .message("문의 사항 삭제 성공")
            .build();
    }
}
