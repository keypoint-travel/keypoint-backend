package com.keypoint.keypointtravel.member.controller;

import com.keypoint.keypointtravel.global.config.security.CustomUserDetails;
import com.keypoint.keypointtravel.global.dto.response.APIResponseEntity;
import com.keypoint.keypointtravel.member.dto.request.EmailRequest;
import com.keypoint.keypointtravel.member.dto.response.IsExistedEmailResponse;
import com.keypoint.keypointtravel.member.dto.response.OtherMemberProfileResponse;
import com.keypoint.keypointtravel.member.dto.response.memberProfile.MemberProfileResponse;
import com.keypoint.keypointtravel.member.dto.useCase.EmailUseCase;
import com.keypoint.keypointtravel.member.dto.useCase.MemberIdUseCase;
import com.keypoint.keypointtravel.member.dto.useCase.OtherMemberUseCase;
import com.keypoint.keypointtravel.member.service.ReadMemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/members")
public class ReadMemberController {

    private final ReadMemberService readMemberService;

    @PostMapping("/email/validate")
    public APIResponseEntity<IsExistedEmailResponse> checkIsExistedEmail(
        @Valid @RequestBody EmailRequest request) {
        EmailUseCase useCase = EmailUseCase.from(request);
        boolean result = readMemberService.checkIsExistedEmail(useCase);

        return APIResponseEntity.<IsExistedEmailResponse>builder()
            .message("이메일 인증 성공")
            .data(IsExistedEmailResponse.from(result))
            .build();
    }

    @PreAuthorize("hasRole('ROLE_CERTIFIED_USER')")
    @GetMapping("/profile")
    public APIResponseEntity<MemberProfileResponse> getMemberProfile(
        @AuthenticationPrincipal CustomUserDetails userDetails) {
        MemberIdUseCase useCase = MemberIdUseCase.from(userDetails.getId());
        MemberProfileResponse result = readMemberService.getMemberProfile(useCase);

        return APIResponseEntity.<MemberProfileResponse>builder()
            .message("사용자 프로필 정보 조회 성공")
            .data(result)
            .build();
    }

    @GetMapping("/profile/{memberId}")
    public APIResponseEntity<OtherMemberProfileResponse> getOtherMemberProfile(
        @PathVariable Long memberId,
        @AuthenticationPrincipal CustomUserDetails userDetails) {
        // 로그인 하지 않은 경우 Authorization 값을 null로 받아 userDetails를 null로 설정(차단 여부 확인을 위해 로그인 여부 확인 필요)
        OtherMemberUseCase useCase = new OtherMemberUseCase(memberId, userDetails);
        OtherMemberProfileResponse result = readMemberService.getOtherMemberProfile(useCase);

        return APIResponseEntity.<OtherMemberProfileResponse>builder()
            .message("다른 회원 프로필 정보 조회 성공")
            .data(result)
            .build();
    }
}
