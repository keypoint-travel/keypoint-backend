package com.keypoint.keypointtravel.inquiry.controller;


import com.keypoint.keypointtravel.global.config.security.CustomUserDetails;
import com.keypoint.keypointtravel.global.dto.response.APIResponseEntity;
import com.keypoint.keypointtravel.global.dto.response.PageResponse;
import com.keypoint.keypointtravel.inquiry.dto.request.InquiryRequest;
import com.keypoint.keypointtravel.inquiry.dto.response.AdminInquiriesResponse;
import com.keypoint.keypointtravel.inquiry.dto.useCase.InquiriesUseCase;
import com.keypoint.keypointtravel.inquiry.dto.useCase.ReplyUseCase;
import com.keypoint.keypointtravel.inquiry.service.AdminInquiryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/v1/inquiries/management")
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
        ReplyUseCase useCase = new ReplyUseCase(inquiryId, request.getContent(), images, userDetails.getId());
        adminInquiryService.answer(useCase);
        return APIResponseEntity.<Void>builder()
            .message("문의 답변하기 성공")
            .build();
    }

    // 1:1 문의 목록 조회
    @GetMapping
    public APIResponseEntity<PageResponse<AdminInquiriesResponse>> findInquiries(
        @AuthenticationPrincipal CustomUserDetails userDetails,
        @RequestParam(name = "sort-by", required = false) String sortBy,
        @RequestParam(name = "direction", required = false, defaultValue = "desc") String direction,
        @PageableDefault(sort = "modifyAt", direction = Sort.Direction.DESC) Pageable pageable) {
        // todo : 관리자 인증 추가
        // sortBy를 제공한 경우, direction 에 따라 정렬 객체 생성
        // sortBy 종류 : "memberName", "content", "isReplied", "modifyAt", "isDeleted"
        if (sortBy != null && !sortBy.isEmpty()) {
            Sort sort = Sort.by(Sort.Direction.fromString(direction), sortBy);
            pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);
        }
        InquiriesUseCase useCase = InquiriesUseCase.of(sortBy, direction, pageable);
        Page<AdminInquiriesResponse> response = adminInquiryService.findInquiries(useCase);
        return APIResponseEntity.toPage(
            "문의 목록 조회 성공",
            response);
    }
}
