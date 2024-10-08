package com.keypoint.keypointtravel.member.controller;

import com.keypoint.keypointtravel.global.config.security.CustomUserDetails;
import com.keypoint.keypointtravel.global.dto.response.APIResponseEntity;
import com.keypoint.keypointtravel.member.dto.request.EmailRequest;
import com.keypoint.keypointtravel.member.dto.response.IsExistedResponse;
import com.keypoint.keypointtravel.member.dto.response.MemberSettingResponse;
import com.keypoint.keypointtravel.member.dto.response.otherMemberProfile.OtherMemberProfileResponse;
import com.keypoint.keypointtravel.member.dto.response.memberProfile.MemberProfileResponse;
import com.keypoint.keypointtravel.member.dto.useCase.EmailUseCase;
import com.keypoint.keypointtravel.member.dto.useCase.MemberIdUseCase;
import com.keypoint.keypointtravel.member.dto.useCase.OtherMemberUseCase;
import com.keypoint.keypointtravel.member.service.ReadMemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/members")
public class ReadMemberController {

    private final ReadMemberService readMemberService;

    @PostMapping("/email/validate")
    public APIResponseEntity<IsExistedResponse> checkIsNotExistedEmail(
        @Valid @RequestBody EmailRequest request) {
        EmailUseCase useCase = EmailUseCase.from(request);
        boolean result = readMemberService.checkIsNotExistedEmail(useCase);

        return APIResponseEntity.<IsExistedResponse>builder()
            .message("등록되지 않은 이메일 확인 성공")
            .data(IsExistedResponse.from(result))
            .build();
    }

    @PreAuthorize("hasRole('ROLE_CERTIFIED_USER')")
    @GetMapping("/profile")
    public APIResponseEntity<MemberProfileResponse> getMemberProfile(
        @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        MemberIdUseCase useCase = MemberIdUseCase.from(userDetails.getId());
        MemberProfileResponse result = readMemberService.getMemberProfile(useCase);

        return APIResponseEntity.<MemberProfileResponse>builder()
            .message("사용자 프로필 정보 조회 성공")
            .data(result)
            .build();
    }

    @PreAuthorize("hasRole('ROLE_CERTIFIED_USER')")
    @GetMapping("/profile/{memberId}")
    public APIResponseEntity<OtherMemberProfileResponse> getOtherMemberProfile(
        @PathVariable Long memberId,
        @AuthenticationPrincipal CustomUserDetails userDetails) {
        OtherMemberUseCase useCase = new OtherMemberUseCase(memberId, userDetails);
        OtherMemberProfileResponse result = readMemberService.getOtherMemberProfile(useCase);

        return APIResponseEntity.<OtherMemberProfileResponse>builder()
            .message("다른 회원 프로필 정보 조회 성공")
            .data(result)
            .build();
    }

    @Deprecated
    @PreAuthorize("hasRole('ROLE_CERTIFIED_USER')")
    @GetMapping("/settings")
    public APIResponseEntity<MemberSettingResponse> getMemberSetting(
        @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        MemberIdUseCase useCase = MemberIdUseCase.from(userDetails.getId());
        MemberSettingResponse result = readMemberService.getMemberSetting(useCase);

        return APIResponseEntity.<MemberSettingResponse>builder()
            .message("사용자 프로필 정보 조회 성공")
            .data(result)
            .build();
    }
}
