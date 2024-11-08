package com.keypoint.keypointtravel.inquiry.controller;


import com.keypoint.keypointtravel.global.config.security.CustomUserDetails;
import com.keypoint.keypointtravel.global.dto.response.APIResponseEntity;
import com.keypoint.keypointtravel.inquiry.dto.request.InquiryRequest;
import com.keypoint.keypointtravel.inquiry.dto.useCase.AdminInquiryUseCase;
import com.keypoint.keypointtravel.inquiry.service.AdminInquiryService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/v1/inquiries/admin")
@RequiredArgsConstructor
public class AdminInquiryController {

    private final AdminInquiryService adminInquiryService;

    // 1:1 문의 답변
    @PostMapping("/{inquiryId}")
    public APIResponseEntity<Void> inquire(
        @PathVariable Long inquiryId,
        @RequestPart(value = "image", required = false) List<MultipartFile> images,
        @RequestPart(value = "content") InquiryRequest request,
        @AuthenticationPrincipal CustomUserDetails userDetails) {
        // todo : 관리자 인증 추가
        AdminInquiryUseCase useCase = new AdminInquiryUseCase(inquiryId, request.getContent(), images, userDetails.getId());
        adminInquiryService.answer(useCase);
        return APIResponseEntity.<Void>builder()
            .message("문의 답변하기 성공")
            .build();
    }
}
