package com.keypoint.keypointtravel.member.controller;

import com.keypoint.keypointtravel.global.dto.response.APIResponseEntity;
import com.keypoint.keypointtravel.member.dto.request.EmailRequest;
import com.keypoint.keypointtravel.member.dto.request.MemberIdRequest;
import com.keypoint.keypointtravel.member.dto.response.IsExistedEmailResponse;
import com.keypoint.keypointtravel.member.dto.response.memberProfile.MemberProfileResponse;
import com.keypoint.keypointtravel.member.dto.useCase.EmailUseCase;
import com.keypoint.keypointtravel.member.dto.useCase.MemberIdUseCase;
import com.keypoint.keypointtravel.member.service.ReadMemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
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
        @Valid @RequestBody MemberIdRequest request) {
        MemberIdUseCase useCase = MemberIdUseCase.from(request);
        MemberProfileResponse result = readMemberService.getMemberProfile(useCase);

        return APIResponseEntity.<MemberProfileResponse>builder()
            .message("사용자 프로필 정보 조회 성공")
            .data(result)
            .build();
    }

}
