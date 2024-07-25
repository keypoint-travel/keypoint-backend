package com.keypoint.keypointtravel.member.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.keypoint.keypointtravel.global.config.security.CustomUserDetails;
import com.keypoint.keypointtravel.global.dto.response.APIResponseEntity;
import com.keypoint.keypointtravel.member.dto.request.MemberProfileRequest;
import com.keypoint.keypointtravel.member.dto.request.UpdateLanguageRequest;
import com.keypoint.keypointtravel.member.dto.request.UpdatePasswordRequest;
import com.keypoint.keypointtravel.member.dto.response.MemberResponse;
import com.keypoint.keypointtravel.member.dto.useCase.MemberProfileUseCase;
import com.keypoint.keypointtravel.member.dto.useCase.UpdatePasswordUseCase;
import com.keypoint.keypointtravel.member.service.UpdateMemberService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import main.java.com.keypoint.keypointtravel.member.dto.request.UpdateProfileRequest;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/members")
public class UpdateMemberController {

    private final UpdateMemberService updateMemberService;

    @PreAuthorize("hasRole('ROLE_UNCERTIFIED_USER')")
    @PatchMapping("/profile")
    public APIResponseEntity<MemberResponse> addMemberProfile(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @Valid @RequestBody MemberProfileRequest request) {
        MemberProfileUseCase useCase = MemberProfileUseCase.of(userDetails.getId(), request);
        MemberResponse result = updateMemberService.registerMemberProfile(useCase);

        return APIResponseEntity.<MemberResponse>builder()
                .message("소셜 로그인 개인 정보 성공")
                .data(result)
                .build();
    }

    @PatchMapping("password/reset")
    public APIResponseEntity<Void> updatePassword(
            @Valid @RequestBody UpdatePasswordRequest request) {
        UpdatePasswordUseCase useCase = UpdatePasswordUseCase.from(request);
        updateMemberService.updateMemberPassword(useCase);

        return APIResponseEntity.<Void>builder()
                .message("비밀번호 업데이트 성공")
                .data(null)
                .build();
    }

    @PreAuthorize("hasRole('ROLE_CERTIFIED_USER')")
    @PatchMapping("profile")
    public APIResponseEntity<Void> updateProfile(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestPart("profileImage") MultipartFile profileImage,
            @Valid @RequestPart UpdateProfileRequest request) {
        UpdateProfileUseCase useCase = UpdateProfileUseCase.from(request);
        updateMemberService.updateMemberProfile(useCase);

        return APIResponseEntity.<Void>builder()
                .message("사용자 프로필 정보 업데이트 성공")
                .data(null)
                .build();
    }

    @PreAuthorize("hasRole('ROLE_CERTIFIED_USER')")
    @PatchMapping("language")
    public APIResponseEntity<Void> updateLanguage(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @Valid @RequestBody UpdateLanguageRequest request) {
        UpdateLanguageUseCase useCase = UpdateLanguageUseCase.of(userDetails.getId(), request);
        updateMemberService.updateMemberLanguage(useCase);

        return APIResponseEntity.<Void>builder()
                .message("사용자 언어 설정 업데이트 성공")
                .data(null)
                .build();
    }
}
