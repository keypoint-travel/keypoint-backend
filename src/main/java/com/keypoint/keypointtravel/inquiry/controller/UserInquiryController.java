package com.keypoint.keypointtravel.inquiry.controller;


import com.keypoint.keypointtravel.global.config.security.CustomUserDetails;
import com.keypoint.keypointtravel.global.dto.response.APIResponseEntity;
import com.keypoint.keypointtravel.inquiry.dto.request.InquiryRequest;
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
        @RequestPart(value = "image", required = false) List<MultipartFile> images,
        @RequestPart(value = "content") InquiryRequest request,
        @AuthenticationPrincipal CustomUserDetails userDetails) {
        InquiryUseCase useCase = new InquiryUseCase(request.getContent(), images, userDetails.getId());
        userInquiryService.inquire(useCase);
        return APIResponseEntity.<Void>builder()
            .message("문의하기 성공")
            .build();
    }
}
