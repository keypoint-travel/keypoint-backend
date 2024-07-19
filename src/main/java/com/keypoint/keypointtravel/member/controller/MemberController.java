package com.keypoint.keypointtravel.member.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.keypoint.keypointtravel.auth.dto.response.EmailVerificationResponse;
import com.keypoint.keypointtravel.global.dto.response.APIResponseEntity;
import com.keypoint.keypointtravel.member.dto.request.EmailRequest;
import com.keypoint.keypointtravel.member.dto.request.EmailVerificationRequest;
import com.keypoint.keypointtravel.member.dto.request.SignUpRequest;
import com.keypoint.keypointtravel.member.dto.response.MemberResponse;
import com.keypoint.keypointtravel.member.dto.useCase.EmailUseCase;
import com.keypoint.keypointtravel.member.dto.useCase.EmailVerificationUseCase;
import com.keypoint.keypointtravel.member.dto.useCase.SignUpUseCase;
import com.keypoint.keypointtravel.member.service.CreateMemberService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/members")
public class MemberController {

    private final CreateMemberService createMemberService;

    @PostMapping("/email/validate")
    public APIResponseEntity<Void> sendVerificationCodeToEmail(@Valid @RequestBody EmailRequest request) {
        EmailUseCase useCase = EmailUseCase.from(request);
        createMemberService.sendVerificationCodeToEmail(useCase);

        return APIResponseEntity.<Void>builder()
                .message("이메일 인증 번호 전송 성공")
                .build();
    }
    
    @PostMapping("/email/confirm")
    public APIResponseEntity<EmailVerificationResponse> confirmEmail(@Valid @RequestBody EmailVerificationRequest request) {
        EmailVerificationUseCase useCase = EmailVerificationUseCase.from(request);
        boolean result = createMemberService.confirmEmail(useCase);

        return APIResponseEntity.<EmailVerificationResponse>builder()
            .message("이메일 인증 성공")
            .data(EmailVerificationResponse.from(result))
            .build();
    }


    @PostMapping
    public APIResponseEntity<MemberResponse> addUser(@Valid @RequestBody SignUpRequest request) {
        SignUpUseCase useCase = SignUpUseCase.from(request);
        MemberResponse result = createMemberService.registerMember(useCase);

        return APIResponseEntity.<MemberResponse>builder()
            .message("회원 가입 성공")
            .data(result)
            .build();
    }
}
