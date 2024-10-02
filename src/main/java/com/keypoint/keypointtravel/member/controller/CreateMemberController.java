package com.keypoint.keypointtravel.member.controller;

import com.keypoint.keypointtravel.badge.service.MemberBadgeService;
import com.keypoint.keypointtravel.global.config.security.CustomUserDetails;
import com.keypoint.keypointtravel.global.dto.response.APIResponseEntity;
import com.keypoint.keypointtravel.global.enumType.setting.BadgeType;
import com.keypoint.keypointtravel.member.dto.request.EmailRequest;
import com.keypoint.keypointtravel.member.dto.request.EmailVerificationRequest;
import com.keypoint.keypointtravel.member.dto.request.MemberProfileRequest;
import com.keypoint.keypointtravel.member.dto.request.SignUpRequest;
import com.keypoint.keypointtravel.member.dto.response.EmailVerificationResponse;
import com.keypoint.keypointtravel.member.dto.response.MemberResponse;
import com.keypoint.keypointtravel.member.dto.useCase.EmailUseCase;
import com.keypoint.keypointtravel.member.dto.useCase.EmailVerificationUseCase;
import com.keypoint.keypointtravel.member.dto.useCase.MemberProfileUseCase;
import com.keypoint.keypointtravel.member.dto.useCase.SignUpUseCase;
import com.keypoint.keypointtravel.member.service.CreateMemberService;
import com.keypoint.keypointtravel.member.service.UpdateMemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/members")
public class CreateMemberController {

    private final UpdateMemberService updateMemberService;
    private final CreateMemberService createMemberService;
    private final MemberBadgeService badgeService;


    @PostMapping("/email/verification-request")
    public APIResponseEntity<Void> sendVerificationCodeToEmail(
        @Valid @RequestBody EmailRequest request) {
        EmailUseCase useCase = EmailUseCase.from(request);
        createMemberService.sendVerificationCodeToEmail(useCase);

        return APIResponseEntity.<Void>builder()
            .message("이메일 인증 번호 전송 성공")
            .build();
    }

    @PostMapping("/email/confirm")
    public APIResponseEntity<EmailVerificationResponse> confirmEmail(
        @Valid @RequestBody EmailVerificationRequest request) {
        EmailVerificationUseCase useCase = EmailVerificationUseCase.from(request);
        boolean result = createMemberService.confirmEmail(useCase);

        return APIResponseEntity.<EmailVerificationResponse>builder()
            .message("이메일 인증 성공")
            .data(EmailVerificationResponse.from(result))
            .build();
    }

    @PostMapping
    public APIResponseEntity<MemberResponse> addMember(@Valid @RequestBody SignUpRequest request) {
        SignUpUseCase useCase = SignUpUseCase.from(request);
        MemberResponse result = createMemberService.registerMember(useCase);

        // 회원가입 배지 발급
        badgeService.earnBadge(result.getId(), BadgeType.SIGN_UP);

        return APIResponseEntity.<MemberResponse>builder()
            .message("회원 가입 성공")
            .data(result)
            .build();
    }

    @PreAuthorize("hasRole('ROLE_UNCERTIFIED_USER')")
    @PutMapping("/profile")
    public APIResponseEntity<MemberResponse> addMemberProfile(
        @AuthenticationPrincipal CustomUserDetails userDetails,
        @Valid @RequestBody MemberProfileRequest request) {
        MemberProfileUseCase useCase = MemberProfileUseCase.of(userDetails.getId(), request);
        MemberResponse result = updateMemberService.registerMemberProfile(useCase);

        // 회원가입 배지 발급
        badgeService.earnBadge(result.getId(), BadgeType.SIGN_UP);

        return APIResponseEntity.<MemberResponse>builder()
            .message("소셜 로그인 개인 정보 성공")
            .data(result)
            .build();
    }
}
