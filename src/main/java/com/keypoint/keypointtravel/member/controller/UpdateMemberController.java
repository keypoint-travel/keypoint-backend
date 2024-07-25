package com.keypoint.keypointtravel.member.controller;

import com.keypoint.keypointtravel.global.config.security.CustomUserDetails;
import com.keypoint.keypointtravel.global.dto.response.APIResponseEntity;
import com.keypoint.keypointtravel.member.dto.request.UpdateLanguageRequest;
import com.keypoint.keypointtravel.member.dto.request.UpdatePasswordRequest;
import com.keypoint.keypointtravel.member.dto.request.UpdateProfileRequest;
import com.keypoint.keypointtravel.member.dto.useCase.UpdateLanguageUseCase;
import com.keypoint.keypointtravel.member.dto.useCase.UpdatePasswordUseCase;
import com.keypoint.keypointtravel.member.dto.useCase.UpdateProfileUseCase;
import com.keypoint.keypointtravel.member.service.UpdateMemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/members")
public class UpdateMemberController {

    private final UpdateMemberService updateMemberService;

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
        @RequestPart(value = "profileImage", required = false) MultipartFile profileImage,
        @Valid @RequestPart(value = "profile") UpdateProfileRequest request
    ) {
        UpdateProfileUseCase useCase = UpdateProfileUseCase.from(
            userDetails.getId(),
            request,
            profileImage);
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
        @Valid @RequestBody UpdateLanguageRequest request
    ) {
        UpdateLanguageUseCase useCase = UpdateLanguageUseCase.of(userDetails.getId(), request);
        updateMemberService.updateMemberLanguage(useCase);

        return APIResponseEntity.<Void>builder()
            .message("사용자 언어 설정 업데이트 성공")
            .data(null)
            .build();
    }
}
