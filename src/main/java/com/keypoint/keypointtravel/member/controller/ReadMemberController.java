package com.keypoint.keypointtravel.member.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.keypoint.keypointtravel.global.dto.response.APIResponseEntity;
import com.keypoint.keypointtravel.member.dto.request.EmailRequest;
import com.keypoint.keypointtravel.member.dto.response.IsExistedEmailResponse;
import com.keypoint.keypointtravel.member.dto.useCase.EmailUseCase;
import com.keypoint.keypointtravel.member.service.ReadMemberService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/members")
public class ReadMemberController {
    private final ReadMemberService memberService;

    @PostMapping("/email/validate")
    public APIResponseEntity<IsExistedEmailResponse> checkIsExistedEmail(
            @Valid @RequestBody EmailRequest request) {
        EmailUseCase useCase = EmailUseCase.from(request);
        boolean result = memberService.checkIsExistedEmail(useCase);

        return APIResponseEntity.<IsExistedEmailResponse>builder()
                .message("이메일 인증 성공")
                .data(IsExistedEmailResponse.from(result))
                .build();
    }

    @GetMapping("/profile")
    public APIResponseEntity<IsExistedEmailResponse> getMemberProfile(
            @Valid @RequestBody EmailRequest request) {
        EmailUseCase useCase = EmailUseCase.from(request);
        boolean result = memberService.checkIsExistedEmail(useCase);

        return APIResponseEntity.<IsExistedEmailResponse>builder()
                .message("사용자 프로필 정보 조회 성공")
                .data(IsExistedEmailResponse.from(result))
                .build();
    }
    
    
}
